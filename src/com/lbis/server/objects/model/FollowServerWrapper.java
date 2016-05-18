package com.lbis.server.objects.model;

import com.lbis.model.Follow;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerSingleWrapper;

public class FollowServerWrapper extends ServerSingleWrapper<Follow> {

	public FollowServerWrapper(ServerMeta meta, Follow data) {
		super(meta, data);
	}

}
