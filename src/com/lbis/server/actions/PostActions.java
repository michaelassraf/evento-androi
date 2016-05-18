package com.lbis.server.actions;

import com.lbis.model.Post;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.PostServerWrapper;
import com.lbis.server.objects.models.PostServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class PostActions extends WebRequester<Post, PostServerWrapper, PostServerMultipleWrapper> {

	@Override
	public Class<Post> getClassType() {
		return Post.class;
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.ShareNewPost;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.Test;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.GetCoolFeed;
	}

	@Override
	public String getClassName() {
		return "Post";
	}

	@Override
	public Class<PostServerWrapper> getSingleClassType() {
		return PostServerWrapper.class;
	}

	@Override
	public Class<PostServerMultipleWrapper> getMultipleClassType() {
		return PostServerMultipleWrapper.class;
	}

	@Override
	public URLs getSearchObjectsUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitializeSincePrefix() {
		return "Init";
	}

	@Override
	public String getPullSincePrefix() {
		return "Pull";
	}

	@Override
	public URLs getObjectsForObjectUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
