package com.lbis.server.objects;

import java.util.LinkedList;

public class ServerMultipleWrapper<CLASSTYPE> {

	ServerMeta meta;
	LinkedList<CLASSTYPE> data;

	public ServerMultipleWrapper(ServerMeta meta, LinkedList<CLASSTYPE> data) {
		this.meta = meta;
		this.data = data;
	}

	public ServerMeta getMeta() {
		return meta;
	}

	public void setMeta(ServerMeta meta) {
		this.meta = meta;
	}

	public LinkedList<CLASSTYPE> getData() {
		return data;
	}

	public void setData(LinkedList<CLASSTYPE> data) {
		this.data = data;
	}

}
