package com.lbis.media.camrecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.capricorn.RayMenu;
import com.lbis.mazeltov.BaseActivity;
import com.lbis.mazeltov.R;
import com.lbis.utils.Utils;

public class RecorderActivity extends BaseActivity {

	static Camera camera;
	Camera.Parameters parameters;
	FrameLayout cameraPreviewFrame;
	RecorderDisplay cameraPreview;
	MediaRecorder mediaRecorder;
	File file;
	RayMenu rayMenu;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	static Integer currentOrientation;

	private final static int MEDIA_FORMAT_MP4 = 1;
	private final static int MEDIA_FORMAT_JPG = 2;
	public static final int START_RECORDING = 6;
	public static final int STOP_RECORDING = 7;
	public static final int TAKE_PICTURE = 8;
	public static final int BACK = 9;

	private int[] startRecordingButton = new int[] { R.drawable.composer_camera, START_RECORDING };
	private int[] stopRecordingButton = new int[] { R.drawable.composer_thought, STOP_RECORDING };
	private int[] takePictureButton = new int[] { R.drawable.composer_thought, TAKE_PICTURE };
	private int[] backButton = new int[] { R.drawable.composer_thought, BACK };

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Utils.getInstance().designActionBar(this, true);
		super.setContentView(R.layout.video_capture);
		cameraPreviewFrame = (FrameLayout) super.findViewById(R.id.camera_preview);
		rayMenu = (RayMenu) findViewById(R.id.ray_menu);

		addButtonToRayMenu(startRecordingButton);
		addButtonToRayMenu(stopRecordingButton);
		addButtonToRayMenu(takePictureButton);
		addButtonToRayMenu(backButton);

		OrientationEventListener mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
			@Override
			public void onOrientationChanged(int orientation) {
				currentOrientation = orientation;
			}
		};

		if (mOrientationEventListener.canDetectOrientation()) {
			mOrientationEventListener.enable();
		}

		toggleButtons(false);
	}

	private void buildCamera() {

		Thread thread = new Thread() {
			public void run() {
				if (camera == null) {
					try {
						camera = Camera.open(0);
					} catch (Exception e) {
						log.error("Failed to bind camera", e);
					}
					if (camera != null) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								initCamera(getApplicationContext());
							}
						});

					}
				}
			}
		};

		thread.start();
	}

	private void addButtonToRayMenu(int[] buttonInfo) {
		int actionID = buttonInfo[1];
		int imageResID = buttonInfo[0];
		ImageView item = new ImageView(this);
		item.setImageResource(imageResID);
		OnClickListener listener = null;
		switch (actionID) {
		case TAKE_PICTURE:
			listener = new OnClickListener() {
				public void onClick(View v) {
					camera.autoFocus(new AutoFocusCallback() {
						@Override
						public void onAutoFocus(boolean arg0, Camera arg1) {
							takePicture();
						}
					});
				}
			};
			break;
		case START_RECORDING:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					startRecording();
					toggleButtons(true);
				}
			};
			break;
		case STOP_RECORDING:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					stopRecording();
					toggleButtons(true);
				}
			};
			break;
		case BACK:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			};
			break;
		}
		rayMenu.addItem(item, listener);
	}

	void toggleButtons(boolean recording) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		buildCamera();
	}

	void initCamera(Context ctx) {
		parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
		camera.setDisplayOrientation(90);
		camera.setParameters(parameters);
		cameraPreview = new RecorderDisplay(ctx, camera);
		cameraPreviewFrame.addView(cameraPreview, 0);
	}

	void releaseCamera() {
		if (camera != null) {
			camera.lock(); // unnecessary in API >= 14
			camera.stopPreview();
			camera.release();
			camera = null;
			cameraPreviewFrame.removeView(cameraPreview);
		}
	}

	void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear configuration (optional here)
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	void releaseResources() {
		releaseMediaRecorder();
		releaseCamera();
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseResources();
	}

	public void startRecording() {
		log.info("Recording started");
		camera.getParameters().set("cam_mode", 1);
		camera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
		camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		camera.setDisplayOrientation(90);
		camera.unlock();
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setCamera(camera);

		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_480P));
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		} else {
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			Camera.Size size = getBestPreviewSize(320, 480, parameters);
			mediaRecorder.setVideoSize(size.width, size.height);
			mediaRecorder.setVideoEncodingBitRate(384000);
			mediaRecorder.setAudioChannels(2);
			mediaRecorder.setAudioSamplingRate(16000);
			mediaRecorder.setAudioEncodingBitRate(32000);
		}
		mediaRecorder.setOrientationHint(90);
		mediaRecorder.setMaxDuration(50000);

		mediaRecorder.setOutputFile(initFile(MEDIA_FORMAT_MP4).getAbsolutePath());
		mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
			Toast.makeText(this, R.string.recording, Toast.LENGTH_SHORT).show();
			toggleButtons(true);
		} catch (Exception e) {
			log.error("Failed to prepare media recorder", e);
			Toast.makeText(this, R.string.cannot_record, Toast.LENGTH_SHORT).show();
			releaseMediaRecorder();
		}
	}

	public void stopRecording() {
		log.info("Recording stopped");
		assert mediaRecorder != null;
		try {
			mediaRecorder.stop();
			Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
		} catch (RuntimeException e) {
			log.error("Failed to record", e);
			if (file != null && file.exists() && file.delete()) {
				log.info("File deleted due the error");
			}
			return;
		} finally {
			releaseMediaRecorder();
		}
		if (file == null || !file.exists()) {
			log.error("File does not exist after stop: " + file.getAbsolutePath());
		} else {
			log.info("Going to display the video: " + file.getAbsolutePath());
			Intent intent = new Intent(this, VideoPlaybackActivity.class);
			intent.putExtra(VideoPlaybackActivity.THUMBNAIL_LOCATION, createThumbnail(file.getAbsolutePath().toString()));
			intent.putExtra(VideoPlaybackActivity.VIDEO_LOCATION, file.getAbsolutePath().toString());
			intent.putExtra(VideoPlaybackActivity.IS_SHARE, true);
			super.startActivity(intent);
		}
	}

	private File initFile(int format) {
		File dir = null;
		String extension = null;
		switch (format) {
		case MEDIA_FORMAT_JPG:
			dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mazeltov pictures");
			extension = "jpg";
			break;
		case MEDIA_FORMAT_MP4:
			dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "mazeltov videos");
			extension = "mp4";
			break;
		}

		if (!dir.exists() && !dir.mkdirs()) {
			log.error("Failed to create storage directory: " + dir.getAbsolutePath());
			Toast.makeText(RecorderActivity.this, R.string.cannot_record, Toast.LENGTH_SHORT).show();
			file = null;
		} else {
			file = new File(dir.getAbsolutePath(), new SimpleDateFormat("'IMG_'yyyyMMddHHmmss'." + extension + "'", Locale.US).format(new Date()));
		}
		return file;
	}

	public static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		if (result == null)
			return parameters.getSupportedPreviewSizes().get(0);
		return (result);
	}

	public void takePicture() {
		Camera.Size size = getBestPreviewSize(Integer.MAX_VALUE, Integer.MAX_VALUE, parameters);
		camera.getParameters().setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.getParameters().setPictureSize(size.width, size.height);
		camera.getParameters().setFocusMode("auto");
		camera.takePicture(shutterCallback, rawCallback, jpegCallback);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// Write to SD Card
				File file = initFile(MEDIA_FORMAT_JPG);
				outStream = new FileOutputStream(file);
				outStream.write(data);
				outStream.close();

				Bitmap bmp = BitmapFactory.decodeFile(file.getPath().toString());
				Matrix matrix = new Matrix();
				int pictureOrie = 0;
				if (currentOrientation > 0 && 45 > currentOrientation)
					pictureOrie = 0;
				else if (currentOrientation >= 45 && 90 >= currentOrientation)
					pictureOrie = 90;
				else if (currentOrientation >= 90 && 135 >= currentOrientation)
					pictureOrie = 90;
				else if (currentOrientation >= 135 && 180 >= currentOrientation)
					pictureOrie = 180;
				else if (currentOrientation >= 180 && 225 >= currentOrientation)
					pictureOrie = 180;
				else if (currentOrientation >= 225 && 270 >= currentOrientation)
					pictureOrie = 270;
				else if (currentOrientation >= 270 && 315 >= currentOrientation)
					pictureOrie = 270;
				switch (pictureOrie) {
				case 0:
					matrix.postRotate(90);
					break;
				case 90:
					matrix.postRotate(180);
					break;
				case 270:
					matrix.postRotate(0);
					break;
				default:
					break;
				}
				bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				FileOutputStream fOut;
				fOut = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
				fOut.flush();
				fOut.close();
				log.info("JPEG Picutre taken - wrote bytes: " + data.length + " Current screen rotation is " + currentOrientation + " and picure rotation was " + pictureOrie);
				Intent intent = new Intent(getApplicationContext(), PictureDisplayActivity.class);
				intent.putExtra("share", true);
				intent.putExtra("file", file.getPath().toString());
				startActivity(intent);
				resetCam();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Problem taking JPEG picutre", e);
			}
		}
	};

	private void resetCam() {
		camera.startPreview();
	}

	public String createThumbnail(String videoPath) {
		try {
			Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
			File thumbFile = new File(getApplicationContext().getFilesDir().toString() + "Tumbnail" + (Math.random() * 1000) + ".png");
			FileOutputStream out = new FileOutputStream(thumbFile);
			thumb.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			return thumbFile.getAbsolutePath().toString();
		} catch (Exception e) {
			return "";
		}
	}

}
