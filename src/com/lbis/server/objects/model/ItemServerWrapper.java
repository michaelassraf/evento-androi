package com.lbis.server.objects.model;

import com.lbis.model.Item;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerSingleWrapper;

public class ItemServerWrapper extends ServerSingleWrapper<Item> {

	public ItemServerWrapper(ServerMeta meta, Item data) {
		super(meta, data);
	}

}
