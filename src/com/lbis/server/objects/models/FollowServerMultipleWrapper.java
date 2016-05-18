package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Follow;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class FollowServerMultipleWrapper extends ServerMultipleWrapper<Follow> {

	public FollowServerMultipleWrapper(ServerMeta meta, LinkedList<Follow> data) {
		super(meta, data);
	}

}
