package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.app.Activity;

import com.lbis.database.executors.model.NotificationDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.NotificationActions;

public class NotificationsUpdateCallable implements Callable<Boolean> {
	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public NotificationsUpdateCallable(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Boolean call() {
		try {
			new NotificationDbExecutors().putAll(activity, new NotificationActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), activity));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing notifications from server", e);
		}
		return false;
	}
}
