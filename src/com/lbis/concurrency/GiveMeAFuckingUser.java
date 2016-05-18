package com.lbis.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.model.User;
import com.lbis.utils.Utils;

public class GiveMeAFuckingUser extends AsyncTask<Void, Void, Boolean> {

	Button button;
	Activity activity;
	User user;
	TextView userNameView;
	TextView userDetailsView;
	ImageView userProfileView;
	Button userFollownButton;
	boolean dontCheckFollow;
	boolean isLoggedInUser;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public GiveMeAFuckingUser(Button button, Activity activity, User user, TextView userNameView, TextView userDetailsView, ImageView userProfileView, Button userFollownButton) {
		this.button = button;
		this.activity = activity;
		this.user = user;
		this.userNameView = userNameView;
		this.userDetailsView = userDetailsView;
		this.userProfileView = userProfileView;
		this.userFollownButton = userFollownButton;
	}

	@Override
	protected void onPreExecute() {
		if (activity != null)
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					button.setText("...");
				}
			});
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		if (!Utils.getInstance().checkIfIsValidUser(user)) {
			Future<Boolean> userUpdate = pool.submit(new UserUpdateCallable(activity, user.getId()));
			try {
				userUpdate.get();
			} catch (Throwable th) {
				log.error("Error while waiting for user update to end", th);
			}
		}

		user = new UserDbExecutors().get(user, activity) == null ? user : new UserDbExecutors().get(user, activity);

		if (activity != null && new ExecuteManagementMethods().getTokenAndUserId(activity).getUserId().equals(this.user.getUserId())) {
			dontCheckFollow = true;
			return true;
		}

		if (activity != null)
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utils.getInstance().displayUser(user, null, isLoggedInUser, userNameView, userDetailsView, userProfileView, userFollownButton, activity);
				}
			});

		Future<Boolean> followUpdate = pool.submit(new FollowsUpdateCallable(activity));
		try {
			followUpdate.get();
			return true;
		} catch (Throwable th) {
			log.fatal("Can't get answer from user and follow thread", th);
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (dontCheckFollow) {
			if (activity != null)
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Utils.getInstance().displayUser(user, null, true, userNameView, userDetailsView, userProfileView, userFollownButton, activity);
					}
				});
			return;
		}
		if (activity != null)
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Utils.getInstance().displayUser(user, Utils.getInstance().checkIfIsFollowing(user.getId(), activity), false, userNameView, userDetailsView, userProfileView, userFollownButton, activity);
				}
			});

		super.onPostExecute(result);
	}

}