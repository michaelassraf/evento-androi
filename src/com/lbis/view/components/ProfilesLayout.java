package com.lbis.view.components;

import java.util.LinkedList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.User;

public class ProfilesLayout extends LinearLayout {

	public ProfilesLayout(String string, LinkedList<User> profiles, Activity activity) {
		super(activity.getApplicationContext());

		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		LinearLayout main = (LinearLayout) inflater.inflate(R.layout.profiles_layout, null);

		TextView textView = (TextView) main.findViewById(R.id.profiles_title);
		textView.setText(string);

		ProfileLayout profileLayout;
		for (User profile : profiles) {
			if (profile.getUserProfilePicture() != null) {
				profileLayout = new ProfileLayout(profile, activity);
				main.addView(profileLayout);
			}
		}

		addView(main);
	}

}
