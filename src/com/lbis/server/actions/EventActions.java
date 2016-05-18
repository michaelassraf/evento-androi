package com.lbis.server.actions;

import java.io.UnsupportedEncodingException;

import android.content.Context;

import com.lbis.model.Event;
import com.lbis.server.WebReciever;
import com.lbis.server.WebRequester;
import com.lbis.server.objects.model.EventServerWrapper;
import com.lbis.server.objects.models.EventServerMultipleWrapper;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class EventActions extends WebRequester<Event, EventServerWrapper, EventServerMultipleWrapper> {

	@Override
	public Class<Event> getClassType() {
		return Event.class;
	}

	@Override
	public URLs createNewRequestUrl() {
		return Enums.URLs.CreateNewEvent;
	}

	@Override
	public URLs getObjectForObjectUrl() {
		return Enums.URLs.GetEvent;
	}

	@Override
	public URLs getAllObjectsSinceUrl() {
		return Enums.URLs.EventSinceUrl;
	}

	@Override
	public String getClassName() {
		return "Event";
	}

	@Override
	public Class<EventServerWrapper> getSingleClassType() {
		return EventServerWrapper.class;
	}

	@Override
	public Class<EventServerMultipleWrapper> getMultipleClassType() {
		return EventServerMultipleWrapper.class;
	}

	@Override
	public URLs getSearchObjectsUrl() {
		return null;
	}

	public Event get(Long eventId, Context context) throws UnsupportedEncodingException, Exception {
		return getObjectForObject(new Event(eventId), WebReciever.getInstance().getConnection(), context);// go and bring
																											// from web
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
		return URLs.Test;
	}
}
