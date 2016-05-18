package com.lbis.model;

import com.lbis.database.model.SimpleIfc;

public class Follow implements SimpleIfc<Follow> {

	private Long followConnectedId;
	private int followAction;

	public Follow(Long followConnectedId, int followAction) {
		this.followConnectedId = followConnectedId;
		this.followAction = followAction;
	}

	public Long getFollowConnectedId() {
		return followConnectedId;
	}

	public void setFollowConnectedId(Long followConnectedId) {
		this.followConnectedId = followConnectedId;
	}

	public int getFollowAction() {
		return followAction;
	}

	public void setFollowAction(int followAction) {
		this.followAction = followAction;
	}

	@Override
	public Class<Follow> getClassType() {
		return Follow.class;
	}

	@Override
	public Long getId() {
		return followConnectedId;
	}
}
