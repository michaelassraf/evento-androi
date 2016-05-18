package com.lbis.model.view;

import org.apache.log4j.Logger;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.lbis.mazeltov.R;

public class PopUpDialog extends Dialog {

	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public PopUpDialog(Context context) {
		super(context, R.style.Theme_Transparent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_popup_display);
		ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
		colorDrawable.setAlpha(130);
		getWindow().setBackgroundDrawable(colorDrawable);
		setCanceledOnTouchOutside(true);
	}
}