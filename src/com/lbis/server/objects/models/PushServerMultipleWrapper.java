package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Push;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class PushServerMultipleWrapper extends ServerMultipleWrapper<Push> {

	public PushServerMultipleWrapper(ServerMeta meta, LinkedList<Push> data) {
		super(meta, data);
	}

}
