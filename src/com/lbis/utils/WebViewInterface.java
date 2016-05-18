package com.lbis.utils;

import android.content.Intent;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.Toast;

import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.lbis.media.camrecorder.PictureDisplayActivity;

public class WebViewInterface {

	private WebView mAppView;

	public WebViewInterface(WebView appView) {
		this.mAppView = appView;
	}

	public void openPictureViewer(String echo) {
		Toast toast = Toast.makeText(mAppView.getContext(), echo, Toast.LENGTH_SHORT);
		toast.show();

		Intent intent = new Intent(mAppView.getContext(), PictureDisplayActivity.class);

		intent.putExtra("url", echo);
		mAppView.getContext().startActivity(intent);
	}

	public void launchImagePicker(String echo) {
		Toast toast = Toast.makeText(mAppView.getContext(), echo, Toast.LENGTH_SHORT);
		toast.show();

		Intent intent = new Intent(mAppView.getContext(), PictureDisplayActivity.class);

		intent.putExtra("url", echo);
		mAppView.getContext().startActivity(intent);
	}

	public void getContentSizes(String width, String height) {
		if (width != null && height != null) {
			final int webviewContentWidth = Integer.parseInt(width);
			final int webviewContentHeight = Integer.parseInt(height);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ObjectAnimator animY = ObjectAnimator.ofInt(mAppView, "scrollY", mAppView.getScrollY(), webviewContentHeight / 2);
					animY.setDuration(600);
					animY.start();
					ObjectAnimator animX = ObjectAnimator.ofInt(mAppView, "scrollX", mAppView.getScrollX(), webviewContentWidth / 2);
					animX.setDuration(600);
					animX.start();
				}
			}, 0);
		}
	}
}