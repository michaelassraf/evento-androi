package com.lbis.database.executors.model;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.User;

public class UserDbExecutors extends ExecuteMethods<User> {

	@Override
	public Class<User> getHandledObjectType() {
		return User.class;
	}

}
