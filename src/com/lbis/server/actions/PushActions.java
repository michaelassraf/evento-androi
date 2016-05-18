package com.lbis.server.actions;

import com.lbis.model.Push;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.PushServerWrapper;
import com.lbis.server.objects.models.PushServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class PushActions extends WebRequester<Push, PushServerWrapper, PushServerMultipleWrapper> {

	@Override
	public Class<Push> getClassType() {
		return Push.class;
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.RegisterPush;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.RegisterPush;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.RegisterPush;
	}

	@Override
	public String getClassName() {
		return "Push";
	}

	@Override
	public Class<PushServerWrapper> getSingleClassType() {
		return PushServerWrapper.class;
	}

	@Override
	public Class<PushServerMultipleWrapper> getMultipleClassType() {
		return PushServerMultipleWrapper.class;
	}

	@Override
	public URLs getSearchObjectsUrl() {
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
