package com.lbis.mazeltov.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.view.PopUpDialog;
import com.lbis.utils.Utils;

public class CustomDialog extends Activity {

	PopupWindow popUp;
	LinearLayout layout;
	TextView tv;
	LayoutParams params;
	LinearLayout mainLayout;
	Button but;
	boolean click = true;
	Activity act;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		but = new Button(this);
		but.setText("Click Me");
		act = this;
		mainLayout = new LinearLayout(this);
		but.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (click) {
					showpopup(0, "chibi", act);
				} else {
					popUp.dismiss();
					click = true;
				}
			}

		});
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mainLayout.addView(but, params);
		setContentView(mainLayout);
	}

	private void showpopup(int popuptype, String message, final Activity act) {

		PopUpDialog dialog = new PopUpDialog(act);

		ImageView imageView = (ImageView) dialog.findViewById(R.id.profile_popup_display_picture);
		Utils.getInstance().loadDesignedImageView(imageView, "http://lbis.dyndns.org/MyProjects/MichaelTest/1.jpg", act.getApplicationContext(), 1, 2);
		TextView tv = (TextView) dialog.findViewById(R.id.profile_popup_display_title);
		tv.setText("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

		TextView tv1 = (TextView) dialog.findViewById(R.id.profile_popup_display_sub_title);
		tv1.setText("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

		TextView tv2 = (TextView) dialog.findViewById(R.id.profile_popup_display_smalll_text);
		tv2.setText("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

		dialog.setCancelable(true);

		dialog.show();

	}

}
