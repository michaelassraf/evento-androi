package com.lbis.server.objects;

import com.lbis.model.Token;

public class ServerMeta {

	private int statusCode;
	private String statusMessage;
	private Token token;

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public ServerMeta(int statusCode, String statusMessage, Token token) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.token = token;
	}

	public ServerMeta() {
	}

	public ServerMeta(Token token) {
		this.token = token;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}