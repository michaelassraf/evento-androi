package com.lbis.database.executors.model;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.Event;

public class EventDbExecutors extends ExecuteMethods<Event> {

	@Override
	public Class<Event> getHandledObjectType() {
		return Event.class;
	}
}
