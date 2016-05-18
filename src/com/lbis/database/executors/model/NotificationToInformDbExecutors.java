package com.lbis.database.executors.model;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.Notification;

public class NotificationToInformDbExecutors extends ExecuteMethods<Notification> {
	@Override
	public Class<Notification> getHandledObjectType() {
		return Notification.class;
	}

	@Override
	public String getTableName() {
		return getHandledObjectType().getSimpleName() + "ToInform";
	}

}
