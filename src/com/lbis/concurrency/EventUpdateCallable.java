package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.content.Context;

import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.server.actions.EventActions;

public class EventUpdateCallable implements Callable<Boolean> {

	Context context;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Long eventId;

	public EventUpdateCallable(Context context, Long eventId) {
		this.context = context;
		this.eventId = eventId;
	}

	@Override
	public Boolean call() {
		try {
			new EventDbExecutors().put(context, new EventActions().get(eventId, context));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing event from server, evend id was " + eventId == null ? "can't get event id" : eventId, e);
		}
		return false;
	}
}
