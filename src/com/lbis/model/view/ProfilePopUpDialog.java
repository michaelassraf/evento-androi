package com.lbis.model.view;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.concurrency.GiveMeAFuckingUser;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.User;
import com.lbis.utils.Utils;

public class ProfilePopUpDialog extends PopUpDialog {

	TextView userNameView;
	TextView userDetailsView;
	ImageView userProfileView;
	Button userFollownButton;
	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	static User user;

	public ProfilePopUpDialog(Activity activity, User user) {
		super(activity);
		setContentView(R.layout.profile_popup_display);
		userNameView = (TextView) findViewById(R.id.profile_popup_display_title);
		userDetailsView = (TextView) findViewById(R.id.profile_popup_display_sub_title);
		userProfileView = (ImageView) findViewById(R.id.profile_popup_display_picture);
		userFollownButton = (Button) findViewById(R.id.profile_follow_button);

		this.user = new UserDbExecutors().get(user, activity) == null ? user : new UserDbExecutors().get(user, activity);

		if (Utils.getInstance().checkIfIsValidUser(this.user)) {
			Utils.getInstance().displayUser(this.user, null, false, userNameView, userDetailsView, userProfileView, userFollownButton, activity);
		}

		if (!Utils.getInstance().checkIfIsFollowing(this.user.getId(), activity) || !Utils.getInstance().checkIfIsValidUser(this.user)) {
			AsyncTaskMap.getAsyncHashMap().cancelAll();
			GiveMeAFuckingUser task = new GiveMeAFuckingUser(userFollownButton, activity, user, userNameView, userDetailsView, userProfileView, userFollownButton);
			task.execute();
			AsyncTaskMap.getAsyncHashMap().add(task);
		} else
			Utils.getInstance().displayUser(this.user, true, false, userNameView, userDetailsView, userProfileView, userFollownButton, activity);
	}
}
