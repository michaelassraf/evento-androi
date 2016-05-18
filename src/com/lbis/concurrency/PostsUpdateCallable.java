package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.app.Activity;

import com.lbis.database.executors.model.PostsDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.PostActions;

public class PostsUpdateCallable implements Callable<Boolean> {
	Activity activity;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public PostsUpdateCallable(Activity activity) {
		this.activity = activity;
	}

	@Override
	public Boolean call() {
		try {
			new PostsDbExecutors().putAll(activity.getApplicationContext(), new PostActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), activity.getApplicationContext()));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing posts from server", e);
		}
		return false;
	}
}
