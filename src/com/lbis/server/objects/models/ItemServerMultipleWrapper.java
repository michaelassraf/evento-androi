package com.lbis.server.objects.models;

import java.util.LinkedList;

import com.lbis.model.Item;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;

public class ItemServerMultipleWrapper extends ServerMultipleWrapper<Item> {

	public ItemServerMultipleWrapper(ServerMeta meta, LinkedList<Item> data) {
		super(meta, data);
	}

}
