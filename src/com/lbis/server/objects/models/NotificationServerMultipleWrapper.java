package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Notification;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class NotificationServerMultipleWrapper extends ServerMultipleWrapper<Notification> {

	public NotificationServerMultipleWrapper(ServerMeta meta, LinkedList<Notification> data) {
		super(meta, data);
	}

}
