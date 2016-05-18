package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.User;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class UserServerMultipleWrapper extends ServerMultipleWrapper<User> {

	public UserServerMultipleWrapper(ServerMeta meta, LinkedList<User> data) {
		super(meta, data);
	}

}
