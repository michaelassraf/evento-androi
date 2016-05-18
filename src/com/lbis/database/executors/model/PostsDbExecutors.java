package com.lbis.database.executors.model;

import com.lbis.database.executors.ExecuteMethods;
import com.lbis.model.Post;

public class PostsDbExecutors extends ExecuteMethods<Post> {

	@Override
	public Class<Post> getHandledObjectType() {
		return Post.class;
	}

}
