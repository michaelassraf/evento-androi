package com.lbis.model;

public class Token {

	private String tokenValue;
	private Long userId;

	public Token(String tokenValue, Long userId) {
		this.tokenValue = tokenValue;
		this.userId = userId;
	}

	public Token() {
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(" Token value ").append(tokenValue).append(" User Id ").append(userId).toString();
	}

}
