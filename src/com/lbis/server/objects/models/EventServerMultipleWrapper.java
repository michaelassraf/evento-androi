package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Event;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class EventServerMultipleWrapper extends ServerMultipleWrapper<Event> {

	public EventServerMultipleWrapper(ServerMeta meta, LinkedList<Event> data) {
		super(meta, data);
	}

}
