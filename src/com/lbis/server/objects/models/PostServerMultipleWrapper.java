package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Post;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class PostServerMultipleWrapper extends ServerMultipleWrapper<Post> {

	public PostServerMultipleWrapper(ServerMeta meta, LinkedList<Post> data) {
		super(meta, data);
	}

}
