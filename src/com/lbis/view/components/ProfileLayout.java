package com.lbis.view.components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lbis.mazeltov.R;
import com.lbis.model.User;
import com.lbis.model.view.ProfilePopUpDialog;
import com.lbis.utils.Utils;

public class ProfileLayout extends LinearLayout {

	ImageView profileImageView;

	public ProfileLayout(final User user, final Activity activity) {
		super(activity.getApplicationContext());
		LayoutInflater inflator = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String profileImageUrl = "";
		if (user != null && user.getUserProfilePicture() != null)
			profileImageUrl = user.getUserProfilePicture().getFormattedUrl() == null ? "" : user.getUserProfilePicture().getFormattedUrl();
		LinearLayout layout = (LinearLayout) inflator.inflate(R.layout.profile_layout, null);
		profileImageView = (ImageView) layout.findViewById(R.id.profile_image);
		Utils.getInstance().loadDesignedImageView(profileImageView, profileImageUrl, activity.getApplicationContext(), 7, 7, false, true, false);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProfilePopUpDialog(activity, user).show();
			}
		});

		addView(layout);
	}
}
