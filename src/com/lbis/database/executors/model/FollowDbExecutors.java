package com.lbis.database.executors.model;

import android.content.Context;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.Follow;

public class FollowDbExecutors extends ExecuteMethods<Follow> {

	@Override
	public Class<Follow> getHandledObjectType() {
		return Follow.class;
	}

	public boolean isFollowing(Long userId, Context context) {
		if (get(userId == null ? 0L : userId, context) != null)
			return true;
		return false;
	}
}