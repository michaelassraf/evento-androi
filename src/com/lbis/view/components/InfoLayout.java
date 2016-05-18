package com.lbis.view.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lbis.mazeltov.R;

public class InfoLayout extends RelativeLayout {

	TextView tvtitle;
	String title;

	TextView tvtext;
	String text;

	public InfoLayout(Context context, String title, String text) {
		super(context);
		this.title = title;
		this.text = text;

		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout main = (RelativeLayout) inflater.inflate(R.layout.info_relative_layout, null);

		tvtitle = (TextView) main.findViewById(R.id.info_title);
		tvtitle.setText(this.title);

		tvtext = (TextView) main.findViewById(R.id.info_text);
		tvtext.setText(this.text);

		addView(main);
	}

}
