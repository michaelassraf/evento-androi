package com.lbis.server.objects.model;

import com.lbis.model.Push;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerSingleWrapper;

public class PushServerWrapper extends ServerSingleWrapper<Push> {

	public PushServerWrapper(ServerMeta meta, Push data) {
		super(meta, data);
	}

}
