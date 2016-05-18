package com.lbis.database.executors.model;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.Notification;

public class NotificationDbExecutors extends ExecuteMethods<Notification> {

	@Override
	public Class<Notification> getHandledObjectType() {
		return Notification.class;
	}

}
