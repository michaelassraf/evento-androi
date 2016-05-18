package com.lbis.server.actions;

import com.lbis.model.Notification;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.NotificationServerWrapper;
import com.lbis.server.objects.models.NotificationServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class NotificationActions extends WebRequester<Notification, NotificationServerWrapper, NotificationServerMultipleWrapper> {

	@Override
	public Class<Notification> getClassType() {
		return Notification.class;
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.Test;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.Test;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.FuckIWantTheNotifications;
	}

	@Override
	public String getClassName() {
		return "Notification";
	}

	@Override
	public Class<NotificationServerWrapper> getSingleClassType() {
		return NotificationServerWrapper.class;
	}

	@Override
	public Class<NotificationServerMultipleWrapper> getMultipleClassType() {
		return NotificationServerMultipleWrapper.class;
	}

	@Override
	public URLs getSearchObjectsUrl() {
		// TODO Auto-generated method stub
		return null;
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
