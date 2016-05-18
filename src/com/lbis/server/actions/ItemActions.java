package com.lbis.server.actions;

import com.lbis.model.Item;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.ItemServerWrapper;
import com.lbis.server.objects.models.ItemServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class ItemActions extends WebRequester<Item, ItemServerWrapper, ItemServerMultipleWrapper> {

	@Override
	public Class<Item> getClassType() {
		return Item.class;
	}

	@Override
	public String getClassName() {
		return "Item";
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.UploadUrl;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.UploadUrl;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.UploadUrl;
	}

	@Override
	public Class<ItemServerWrapper> getSingleClassType() {
		return ItemServerWrapper.class;
	}

	@Override
	public Class<ItemServerMultipleWrapper> getMultipleClassType() {
		return ItemServerMultipleWrapper.class;
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
