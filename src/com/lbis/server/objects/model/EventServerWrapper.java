package com.lbis.server.objects.model;

import com.lbis.model.Event;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerSingleWrapper;

public class EventServerWrapper extends ServerSingleWrapper<Event> {

	public EventServerWrapper(ServerMeta meta, Event data) {
		super(meta, data);
	}

}
