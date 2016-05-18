package com.lbis.media.camrecorder;

import static com.lbis.utils.Utils.DISCARD;
import static com.lbis.utils.Utils.SHARE;

import org.apache.log4j.Logger;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.capricorn.RayMenu;
import com.lbis.mazeltov.BaseActivity;
import com.lbis.mazeltov.R;
import com.lbis.utils.Utils;

public class PictureDisplayActivity extends BaseActivity {

	private ImageView pictureView;
	private PhotoViewAttacher pictureAttacher;
	private RayMenu rayMenu;
	private String localPicturePath;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	private int[] discardPictureButton = new int[] { R.drawable.composer_camera, DISCARD };
	private int[] sharePictureButton = new int[] { R.drawable.composer_thought, SHARE };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		Utils.getInstance().designActionBar(this, true);

		setContentView(R.layout.picture_viewer);

		pictureView = (ImageView) findViewById(R.id.iv_photo);
		rayMenu = (RayMenu) findViewById(R.id.ray_menu);

		if (getIntent().getExtras().getString("url") != null) {
			Utils.getInstance().loadDesignedImageView(pictureView, getIntent().getExtras().getString("url"), getApplicationContext(), 1, 1, false, true, false);
			log.info("Displayed file is : " + getIntent().getExtras().getString("url"));
		}

		if (getIntent().getExtras().getString("file") != null) {
			localPicturePath = getIntent().getExtras().getString("file");
			Utils.getInstance().loadDesignedImageView(pictureView, getIntent().getExtras().getString("file"), getApplicationContext(), 1, 1, false, false, true);
			log.info("Displayed file is : " + getIntent().getExtras().getString("file"));
		}

		if (getIntent().getExtras().getBoolean("share", false)) {
			Utils.getInstance().addMediaButtonToRayMenu(sharePictureButton, this, rayMenu, localPicturePath, null);
			Utils.getInstance().addMediaButtonToRayMenu(discardPictureButton, this, rayMenu, localPicturePath, null);

			// addButtonToRayMenu();
		} else
			rayMenu.setVisibility(View.INVISIBLE);

		pictureAttacher = new PhotoViewAttacher(pictureView);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					pictureAttacher.setScaleType(ScaleType.FIT_CENTER);
				} catch (InterruptedException e) {
					log.error("Error while waiting to picture attacher ", e);
				}

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pictureAttacher.cleanup();
	}
}
