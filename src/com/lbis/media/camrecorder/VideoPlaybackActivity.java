package com.lbis.media.camrecorder;

import static com.lbis.utils.Utils.DISCARD;
import static com.lbis.utils.Utils.SHARE;

import java.io.File;

import org.apache.log4j.Logger;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.capricorn.RayMenu;
import com.lbis.mazeltov.BaseActivity;
import com.lbis.mazeltov.R;
import com.lbis.utils.Utils;

public class VideoPlaybackActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener {

	VideoView videoPlaybackView;
	FrameLayout videoPlaybackRootView;
	RayMenu rayMenu;
	String videoLocation;
	String thumbnailLocation;
	GestureDetector gestureDetector;
	RelativeLayout videoProgressBarContainer;
	int maxTries = 5;
	boolean isPaused = false;
	int pausedPosition = 0;

	public static final String THUMBNAIL_LOCATION = "thumb_loc";
	public static final String VIDEO_LOCATION = "video_loc";
	public static final String IS_SHARE = "is_share";

	public static final int UPLOAD_VIDEO = 6;
	public static final int DELETE_VIDEO = 7;
	public static final int BACK = 9;

	private int[] discardPictureButton = new int[] { R.drawable.composer_camera, DISCARD };
	private int[] sharePictureButton = new int[] { R.drawable.composer_thought, SHARE };

	final Logger log = Logger.getLogger(getClass().getSimpleName());

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Utils.getInstance().designActionBar(this, true);
		super.setContentView(R.layout.video_playback);
		rayMenu = (RayMenu) super.findViewById(R.id.ray_menu);
		this.videoPlaybackRootView = (FrameLayout) super.findViewById(R.id.video_playback_root_layout);
		this.videoPlaybackView = (VideoView) super.findViewById(R.id.video);
		videoLocation = super.getIntent().getStringExtra(VIDEO_LOCATION);
		thumbnailLocation = super.getIntent().getStringExtra(THUMBNAIL_LOCATION);

		videoProgressBarContainer = (RelativeLayout) findViewById(R.id.video_progress_bar_container);
		if (getIntent().getBooleanExtra(IS_SHARE, false)) {
			Utils.getInstance().addMediaButtonToRayMenu(sharePictureButton, this, rayMenu, thumbnailLocation, videoLocation);
			Utils.getInstance().addMediaButtonToRayMenu(discardPictureButton, this, rayMenu, thumbnailLocation, videoLocation);
		}
		log.info("Playback video is " + videoLocation);
		gestureDetector = new GestureDetector(this, new GestureListener());
		videoPlaybackRootView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		// Pause/resume on single tap
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (!isPaused) {
				pausedPosition = videoPlaybackView.getCurrentPosition();
				videoPlaybackView.pause();
				log.info("Tapped once ! pausing !");
			}
			if (isPaused) {
				videoPlaybackView.seekTo(pausedPosition);
				videoPlaybackView.start();
				log.info("Tapped once ! resuming !");
			}
			isPaused = !isPaused;
			return true;
		}

		// Restart playback on double tap
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			videoPlaybackView.pause();
			videoPlaybackView.seekTo(0);
			videoPlaybackView.start();
			isPaused = false;
			log.info("Tapped twice ! restarting playback!");
			return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.videoPlaybackView.setVideoPath(videoLocation);
		this.videoPlaybackView.setKeepScreenOn(true);
		this.videoPlaybackView.setDrawingCacheEnabled(true);
		this.videoPlaybackView.setOnPreparedListener(this);
		this.videoPlaybackView.setOnErrorListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.videoPlaybackView.stopPlayback();
		isPaused = true;
	}

	public void onPrepared(MediaPlayer mp) {
		log.info("Playback preparation finished, starting playback");
		this.videoPlaybackView.setOnCompletionListener(this);
		this.videoPlaybackView.start();
		videoProgressBarContainer.setVisibility(View.INVISIBLE);
		isPaused = false;
	}

	public void onCompletion(MediaPlayer mp) {
		log.info("Finished playback, sending playback to strat");
		pausedPosition = 0;
		isPaused = true;
	}

	// gets called by the button press
	public void delete(View v) {
		if (new File(videoLocation).delete()) {
			log.info("Media player deleted: " + videoLocation);
			this.notifyUser(R.string.deleted);
		} else {
			log.error("Media player failed to delete: " + videoLocation);
			this.notifyUser(R.string.cannot_delete);
		}
		log.info("Media player going back");
		super.finish();
	}

	void notifyUser(int messageResource) {
		Toast.makeText(this, messageResource, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		log.error("Media player has an error code " + what + " extra code is " + extra);
		if (maxTries > 0) {
			maxTries--;
			onPrepared(mp);
		}
		log.error("Will try again");
		return false;
	}

}
