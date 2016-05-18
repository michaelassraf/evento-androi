package com.lbis.media.camrecorder;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RecorderDisplay extends SurfaceView implements SurfaceHolder.Callback {
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	private final Camera camera;

	public RecorderDisplay(Context context, Camera camera) {
		super(context);
		this.camera = camera;
		super.getHolder().addCallback(this);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			super.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		log.info("Media recorder surface created");
		try {
			this.camera.setPreviewDisplay(holder);
			this.camera.startPreview();
		} catch (IOException e) {
			log.error("Failed to start media recorder surface", e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		log.info("Media recorder surface destroyed");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		log.info("Media recorder surface changed");
	}

}
