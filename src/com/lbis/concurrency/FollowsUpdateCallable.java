package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.app.Activity;

import com.lbis.database.executors.model.FollowDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.FollowActions;

public class FollowsUpdateCallable implements Callable<Boolean> {

	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public FollowsUpdateCallable(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Boolean call() {
		try {
			new FollowDbExecutors().putAll(activity, new FollowActions().getAllMyFollowers(WebReciever.getInstance().getConnection(), activity));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing events from server", e);
		}
		return false;
	}
}
