package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.app.Activity;

import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.EventActions;

public class EventsUpdateCallable implements Callable<Boolean> {

	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public EventsUpdateCallable(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Boolean call() {
		try {
			new EventDbExecutors().putAll(activity, new EventActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), activity));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing events from server", e);
		}
		return false;
	}
}
