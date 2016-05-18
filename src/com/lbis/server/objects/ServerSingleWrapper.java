package com.lbis.server.objects;

public class ServerSingleWrapper<CLASSTYPE> {

	ServerMeta meta;
	CLASSTYPE data;

	public ServerSingleWrapper(ServerMeta meta, CLASSTYPE data) {
		this.meta = meta;
		this.data = data;
	}

	public ServerMeta getMeta() {
		return meta;
	}

	public void setMeta(ServerMeta meta) {
		this.meta = meta;
	}

	public CLASSTYPE getData() {
		return data;
	}

	public void setData(CLASSTYPE data) {
		this.data = data;
	}

}
