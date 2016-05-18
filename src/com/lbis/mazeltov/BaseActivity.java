package com.lbis.mazeltov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.mazeltov.event.EventPictureChoserDialogFragment;
import com.lbis.user.connect.UserFacebookDialogFragment;
import com.lbis.user.connect.UserSignUpDialogFragment;

public class BaseActivity extends SherlockFragmentActivity {

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		EventPictureChoserDialogFragment eventPictureChoserFragment = (EventPictureChoserDialogFragment) getSupportFragmentManager().findFragmentByTag(EventPictureChoserDialogFragment.class.getName());
		if (eventPictureChoserFragment != null) {
			eventPictureChoserFragment.onActivityResult(requestCode, resultCode, intent);
		}

		UserSignUpDialogFragment userSignUpDialogFragment = (UserSignUpDialogFragment) getSupportFragmentManager().findFragmentByTag(UserSignUpDialogFragment.class.getName());
		if (userSignUpDialogFragment != null) {
			userSignUpDialogFragment.onActivityResult(requestCode, resultCode, intent);
		}

		UserFacebookDialogFragment userFacebookDialogFragment = (UserFacebookDialogFragment) getSupportFragmentManager().findFragmentByTag(UserFacebookDialogFragment.class.getName());
		if (userFacebookDialogFragment != null) {
			userFacebookDialogFragment.onActivityResult(requestCode, resultCode, intent);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	static Activity activity;

	public static void saveMyAcitivity(Activity activity) {
		BaseActivity.activity = activity;
	}

	public static Activity getMyActivity() {
		return activity;
	}

}
