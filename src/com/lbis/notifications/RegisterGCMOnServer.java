package com.lbis.notifications;

import org.apache.log4j.Logger;

import android.content.Context;
import android.os.AsyncTask;

import com.lbis.model.Push;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.PushActions;

public class RegisterGCMOnServer extends AsyncTask<String, Void, Void> {
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Context context;

	public RegisterGCMOnServer(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			Push pushObject = new PushActions().createNewRequest(new Push(params[0], 1), WebReciever.getInstance().getConnection(), context);
			pushObject.getPushToken();
		} catch (Exception e) {
			log.error("Error occured while trying to register GCM info on server - ", e);
		}
		return null;
	}

}