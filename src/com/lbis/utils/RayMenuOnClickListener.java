package com.lbis.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.mazeltov.MainActivity;
import com.lbis.media.camrecorder.RecorderActivity;
import com.lbis.view.components.HomeFragment;
import com.lbis.view.components.MyProfileFragement;
import com.lbis.view.components.NotificationsFragment;
import com.lbis.view.components.UpcomingEventsFragment;

public class RayMenuOnClickListener implements OnClickListener {

	private int action;
	private FragmentActivity activity;

	public RayMenuOnClickListener(int desiredAction, FragmentActivity activity) {
		this.action = desiredAction;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {

		switch (action) {
		case MainActivity.MOMENT_CLICK:
			Intent medieRecorder = new Intent(activity, RecorderActivity.class);
			medieRecorder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(medieRecorder);
			break;
		case MainActivity.NEWS_CLICK:
			Bundle args = new Bundle();
			args.putLong(MyProfileFragement.USER_ID_IDENTFIRER, new ExecuteManagementMethods().getTokenAndUserId(activity).getUserId());
			MyProfileFragement newfrag = new MyProfileFragement();
			newfrag.setArguments(args);
			Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), newfrag);
			break;
		case MainActivity.HOME_CLICK:
			Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), new HomeFragment());
			break;
		case MainActivity.UPCOMING_CLICK:
			Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), new UpcomingEventsFragment());
			break;
		case MainActivity.SETTINGS_CLICK:
			Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), new NotificationsFragment());
			break;
		}
	}

}
