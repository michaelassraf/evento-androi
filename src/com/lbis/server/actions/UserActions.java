package com.lbis.server.actions;

import java.io.UnsupportedEncodingException;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.model.User;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.UserServerWrapper;
import com.lbis.server.objects.models.UserServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;
import com.lbis.utils.Utils;

public class UserActions extends WebRequester<User, UserServerWrapper, UserServerMultipleWrapper> {

	@Override
	public Class<User> getClassType() {
		return User.class;
	}

	public User logIn(User user, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return getObjectForObject(user, connection, context, getLoginUrl());
	}

	public User signUp(User user, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return createNewRequest(user, connection, context);
	}

	public User getUserDetails(long userId, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		User user = new UserDbExecutors().get(userId, context);
		if (user != null && Utils.getInstance().checkIfIsValidUser(user))
			return user;
		user = getObjectForObject(new User(userId), connection, context);
		new UserDbExecutors().put(context, user);
		return user;
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.SignUp;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.UserObjectForObjectUrl;
	}

	public URLs getLoginUrl() {
		return Enums.URLs.LogIn;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.Test;
	}

	@Override
	public String getClassName() {
		return "User";
	}

	@Override
	public Class<UserServerWrapper> getSingleClassType() {
		return UserServerWrapper.class;
	}

	@Override
	public Class<UserServerMultipleWrapper> getMultipleClassType() {
		return UserServerMultipleWrapper.class;
	}

	@Override
	public URLs getSearchObjectsUrl() {
		return Enums.URLs.UserSearchUrl;
	}

	@Override
	public String getInitializeSincePrefix() {
		return "";
	}

	@Override
	public String getPullSincePrefix() {
		return "";
	}

	@Override
	public URLs getObjectsForObjectUrl() {
		// TODO Auto-generated method stub
		return null;
	}
}
