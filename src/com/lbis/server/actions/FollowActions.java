package com.lbis.server.actions;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.model.Follow;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.FollowServerWrapper;
import com.lbis.server.objects.models.FollowServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class FollowActions extends WebRequester<Follow, FollowServerWrapper, FollowServerMultipleWrapper> {

	@Override
	public Class<Follow> getClassType() {
		return Follow.class;
	}

	@Override
	public String getClassName() {
		return "Follow";
	}

	@Override
	public URLs createNewRequestUrl() {
		return URLs.FollowURL;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return URLs.FollowURL;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return URLs.FollowURL;
	}

	@Override
	public URLs getSearchObjectsUrl() {
		return URLs.FollowURL;
	}

	@Override
	public Class<FollowServerWrapper> getSingleClassType() {
		return FollowServerWrapper.class;
	}

	@Override
	public Class<FollowServerMultipleWrapper> getMultipleClassType() {
		return FollowServerMultipleWrapper.class;
	}

	@Override
	public String getInitializeSincePrefix() {
		return "";
	}

	@Override
	public String getPullSincePrefix() {
		return "";
	}

	public Follow switchStates(Long distintId, Enums.UsersConnection action, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return super.getObjectForObject(new Follow(distintId, action.getValue()), connection, context);
	}

	public LinkedList<Follow> getAllMyFollowers(DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return super.getObjectsForObject(new Follow(new ExecuteManagementMethods().getTokenAndUserId(context).getUserId(), Enums.UsersConnection.GetFollowers.getValue()), connection, context);
	}

	@Override
	public URLs getObjectsForObjectUrl() {
		return URLs.GetAllFollows;
	}
}
