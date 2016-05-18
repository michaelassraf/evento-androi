package com.lbis.model;

import java.util.LinkedList;

import android.text.format.DateUtils;

import com.lbis.database.model.KeyObjectIfc;
import com.lbis.database.model.SimpleIfc;
import com.lbis.database.model.ValueObjectAbs;
import com.lbis.model.view.ListItemAbs;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.EventType;
import com.lbis.utils.GenericValidator;
import com.lbis.utils.Utils;

public class Event extends ValueObjectAbs<Event> implements KeyObjectIfc, SimpleIfc<Event>, ListItemAbs {

	private Long eventId;
	private Enums.EventType eventType;
	private Long eventOrganizer;
	private User eventHoster;
	private String eventLocation;
	private String eventName;
	private Item eventPicture;
	private String eventDescription;
	private Long eventStartDate;
	private Long eventEndDate;
	private LinkedList<User> eventUsers;
	private LinkedList<Post> eventPosts;

	public Event(long eventId, EventType eventType, Long eventOrganizer, String eventName, Item eventPicture, String eventDescription, long eventStartDate, long eventEndDate, LinkedList<User> eventUsers, LinkedList<Post> eventPosts) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.eventOrganizer = eventOrganizer;
		this.eventName = eventName;
		this.eventPicture = eventPicture;
		this.eventDescription = eventDescription;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventPosts = eventPosts;
		this.eventUsers = eventUsers;
	}

	public String partialValidation() {
		String validatationResult = null;

		if (GenericValidator.isBlankOrNull(eventName))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Event name cannot be empty");

		if (GenericValidator.isBlankOrNull(eventDescription))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Event agenda cannot be empty");

		if (eventStartDate == null)
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Event start date cannot be empty");

		if (eventType == null)
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Event type cannot be empty");

		if (eventLocation == null)
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Event type cannot be empty");

		return validatationResult;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public Item getEventPicture() {
		return eventPicture;
	}

	public void setEventPicture(Item eventPicture) {
		this.eventPicture = eventPicture;
	}

	public Event() {
	}

	public Event(long eventId) {
		this.eventId = eventId;
	}

	public Long getEventOrganizer() {
		return eventOrganizer;
	}

	public void setEventOrganizer(Long eventOrganizer) {
		this.eventOrganizer = eventOrganizer;
	}

	public long getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(long eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public long getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(long eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Enums.EventType getEventType() {
		return eventType;
	}

	public void setEventType(Enums.EventType eventType) {
		this.eventType = eventType;
	}

	public Long getOrganizer() {
		return eventOrganizer;
	}

	public void setOrganizer(Long organizer) {
		this.eventOrganizer = organizer;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public long getStartDate() {
		return eventStartDate;
	}

	public void setStartDate(long startDate) {
		this.eventStartDate = startDate;
	}

	public void setEndDate(long endDate) {
		this.eventEndDate = endDate;
	}

	public LinkedList<User> getEventUsers() {
		return eventUsers;
	}

	public void setEventUsers(LinkedList<User> eventUsers) {
		this.eventUsers = eventUsers;
	}

	public User getEventHoster() {
		return eventHoster;
	}

	public void setEventHoster(User eventHoster) {
		this.eventHoster = eventHoster;
	}

	public LinkedList<Post> getEventPosts() {
		return eventPosts;
	}

	public void setEventPosts(LinkedList<Post> eventPosts) {
		this.eventPosts = eventPosts;
	}

	@Override
	public Class<Event> getClassType() {
		return Event.class;
	}

	@Override
	public long getObjectKey() {
		return getEventId();
	}

	@Override
	public Long getId() {
		return this.eventId;
	}

	@Override
	public String getMainText() {
		return getEventName();
	}

	@Override
	public String getSubText() {
		return getEventDescription();
	}

	@Override
	public String getPicturePath() {
		if (getEventPicture() == null)
			return null;
		return getEventPicture().getFormattedUrl();
	}

	public long getEventTime() {
		return getEventStartDate();
	}

	public String getTimeToEvent() {
		return DateUtils.getRelativeTimeSpanString(getEventTime() * 1000).toString();
	}

	@Override
	public String getCoolText() {
		return new StringBuilder().append("You have been invited to ").append(getEventName()).append(" by ").append(getEventHoster().getMainText()).append(" !").toString();
	}

	@Override
	public Long getClickListenerId() {
		return getEventId();
	}

	@Override
	public Item getItem() {
		return getEventPicture();
	}

	@Override
	public String getSearchText() {
		return "";
	}

	@Override
	public String getAddedText() {
		return "";
	}

}
