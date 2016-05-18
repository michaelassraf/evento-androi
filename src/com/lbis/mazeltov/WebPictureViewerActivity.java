package com.lbis.mazeltov;

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.lbis.utils.HTMLCreator;
import com.lbis.utils.Utils;

/**
 * Load images into a WebView
 * 
 * Sue Smith Mobiletuts+ Tutorial - Android SDK: Using the Android WebView for Image Display and Interaction July 2012
 * 
 */
public class WebPictureViewerActivity extends BaseActivity implements OnClickListener {

	// instance variables
	private final int IMG_PICK = 1;
	private WebView picView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		Utils.getInstance().designActionBar(this, true);
		setContentView(R.layout.web_image_view);
		// get references to UI items and set listeners
		picView = (WebView) findViewById(R.id.pic_view);
		// Button pickBtn = (Button)findViewById(R.id.pick_btn);
		// pickBtn.setOnClickListener(this);
		//
		// Button loadBtn = (Button)findViewById(R.id.load_btn);
		// loadBtn.setOnClickListener(this);
		//
		// Button appBtn = (Button)findViewById(R.id.app_btn);
		// appBtn.setOnClickListener(this);

		// picView.setLayoutParams(new LayoutParams(getWidth(),
		// LayoutParams.MATCH_PARENT));

		picView.getSettings().setBuiltInZoomControls(true);
		picView.getSettings().setUseWideViewPort(true);
		picView.setBackgroundColor(Color.TRANSPARENT);

		picView.getSettings().setLoadWithOverviewMode(true);

		if (getIntent().getExtras().getString("url") != null) {
			String data = null;
			try {
				data = HTMLCreator.getInstance().getFullScreenSizedImage(new URL(getIntent().getExtras().getString("url")));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			picView.loadData(data, "text/html; charset=UTF-8", null);

		}
	}

	/**
	 * respond to button clicks
	 */
	public void onClick(View v) {
		// pick from gallery
		// if (v.getId() == R.id.pick_btn) {
		//
		// Intent pickIntent = new Intent();
		// pickIntent.setType("image/*");
		// pickIntent.setAction(Intent.ACTION_GET_CONTENT);
		// startActivityForResult(Intent.createChooser(pickIntent,
		// "Select Picture"), IMG_PICK);
		//
		// }
		// //load from web
		// else if(v.getId() == R.id.load_btn) {
		// picView.loadUrl("http://developer.android.com/images/brand/google_play_logo_450.png");
		//
		// }
		// //load from app package
		// else if(v.getId() == R.id.app_btn) {
		//
		// picView.loadUrl("file:///android_asset/imagepage.html");
		//
		// **OR THIS
		// picView.loadUrl("file:///android_asset/mypicture.jpg");
		// }

	}

	/**
	 * return from choosing an image in the Gallery
	 */
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == RESULT_OK) {
	// // check back from picture selection
	// if (requestCode == IMG_PICK) {
	// // the returned image URI
	// Uri pickedUri = data.getData();
	// // get path
	// String imagePath = "";
	// String[] imgData = { MediaStore.Images.Media.DATA };
	// // query the data
	// Cursor imgCursor = getContentResolver().query(pickedUri,
	// imgData, null, null, null);
	// if (imgCursor != null) {
	// // get the path string
	// int index = imgCursor
	// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// imgCursor.moveToFirst();
	// imagePath = imgCursor.getString(index);
	// } else
	// imagePath = pickedUri.getPath();
	//
	// // load into webview
	// picView.loadUrl("file:///" + imagePath);
	// }
	// }
	// }
}
