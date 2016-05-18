package com.lbis.model;

public class Push {

	String pushToken;
	int pushAction;

	public Push(String pushToken, int pushAction) {
		this.pushToken = pushToken;
		this.pushAction = pushAction;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public int getPushAction() {
		return pushAction;
	}

	public void setPushAction(int pushAction) {
		this.pushAction = pushAction;
	}

}
