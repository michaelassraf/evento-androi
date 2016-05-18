package com.lbis.concurrency;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import android.content.Context;

import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.UserActions;

public class UserUpdateCallable implements Callable<Boolean> {

	Context context;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Long userId;

	public UserUpdateCallable(Context context, Long userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public Boolean call() {
		try {
			new UserDbExecutors().put(context, new UserActions().getUserDetails(userId, WebReciever.getInstance().getConnection(), context));
			return true;
		} catch (Exception e) {
			log.error("Failed bringing user from server, user id was " + userId == null ? "can't get event id" : userId, e);
		}
		return false;
	}
}
