package com.lbis.concurrency;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;

import com.lbis.database.executors.model.FollowDbExecutors;
import com.lbis.model.Follow;
import com.lbis.model.User;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.FollowActions;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.UsersConnection;
import com.lbis.utils.Utils;

public class SwitchUsersConnectionState extends AsyncTask<Void, Void, Boolean> {

	Button button;
	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	User user;

	public SwitchUsersConnectionState(Button button, Activity activty, User user) {
		this.button = button;
		this.activity = activty;
		this.user = user;
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
		Enums.UsersConnection action = UsersConnection.Follow;
		if (Utils.getInstance().checkIfIsFollowing(user.getId(), activity)) {
			action = UsersConnection.Following;
			try {
				new FollowActions().switchStates(user.getId(), action, WebReciever.getInstance().getConnection(), this.activity);
				new FollowDbExecutors().drop(activity, new Follow(user.getId(), action.getValue()));
			} catch (Exception e) {
				log.error("Can't post data about " + action.toString() + " for user " + user.getId(), e);
				return false;
			}
			return true;
		}

		action = UsersConnection.Follow;
		try {
			new FollowActions().switchStates(user.getId(), action, WebReciever.getInstance().getConnection(), this.activity);
			new FollowDbExecutors().put(activity, new Follow(user.getId(), action.getValue()));
		} catch (Exception e) {
			log.error("Can't post data about " + action.toString() + " for user " + user.getId(), e);
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (activity != null)
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					button.setText(Utils.getInstance().checkIfIsFollowingString(user.getId(), activity));
				}
			});
		super.onPostExecute(result);
	}
}
