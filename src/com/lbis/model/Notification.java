package com.lbis.model;

import android.content.Context;
import android.text.format.DateUtils;

import com.lbis.database.model.KeyObjectIfc;
import com.lbis.database.model.SimpleIfc;
import com.lbis.database.model.ValueObjectAbs;
import com.lbis.model.view.ListItemAbs;
import com.lbis.utils.Enums;
import com.lbis.utils.Utils;

public class Notification extends ValueObjectAbs<Notification> implements KeyObjectIfc, SimpleIfc<Notification>, ListItemAbs {

	private Long notificationId;
	private Integer notificationType;
	private Long notificationConnectedId;
	private Long notificationDate;
	private String notificationName;
	private User notificationFollow;
	private Event notificationInvitation;
	private Post notificationPost;

	public Notification(long notificationId, int notificationType, long notificationDate, String notificationName) {
		this.notificationId = notificationId;
		this.notificationType = notificationType;
		this.notificationDate = notificationDate;
		this.notificationName = notificationName;
	}

	public <T extends ListItemAbs, KeyObjectIfc, ValueObjectAbs> T getNotificationData() {
		switch (getNotificationTypeEnum()) {
		case Follow:
			return (T) notificationFollow;
		case Invitation:
			return (T) notificationInvitation;
		case Post:
			return (T) notificationPost;
		}
		return null;
	}

	public User getNotificationFollow() {
		return notificationFollow;
	}

	public void setNotificationFollow(User notificationFollow) {
		this.notificationFollow = notificationFollow;
	}

	public Post getNotificationPost() {
		return notificationPost;
	}

	public void setNotificationPost(Post notificationPost) {
		this.notificationPost = notificationPost;
	}

	public Notification() {
	}

	public long getNotificationId() {
		return notificationId;
	}

	public Long getNotificationConnectedId() {
		return notificationConnectedId;
	}

	public void setNotificationConnectedId(Long notificationConnectedId) {
		this.notificationConnectedId = notificationConnectedId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public Enums.NotificationType getNotificationTypeEnum() {
		return Enums.NotificationType.values()[notificationType - 1];
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public long getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(long notificationDate) {
		this.notificationDate = notificationDate;
	}

	@Override
	public Class<Notification> getClassType() {
		return Notification.class;
	}

	@Override
	public long getObjectKey() {
		return getNotificationId();
	}

	@Override
	public Long getId() {
		return this.notificationId;
	}

	@Override
	public String getMainText() {
		return this.notificationName;
	}

	@Override
	public String getSubText() {
		return getNotificationData().getSubText();
	}

	@Override
	public String getPicturePath() {
		return getNotificationData().getPicturePath();
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}

	public void setNotificationDate(Long notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getTimeFromNotification() {
		return DateUtils.getRelativeTimeSpanString(getNotificationDate() * 1000).toString();
	}

	public String generatePostTitle(Post post) {
		return new StringBuilder().append(post.getPostPoster().getUserFirstName()).append(" posted something on ").append(post.getPostEventName()).toString();
	}

	public String generatePostSubTitle(Post post) {
		return post.getPostTitle();
	}

	public String generateFollowTitle(User user) {
		return new StringBuilder().append(user.getMainText()).append(" Started following you !").toString();
	}

	public String generateUserSubTitle(User user, Context context) {
		return new StringBuilder().append(Utils.getInstance().calcAge(user.getUserBirthday())).append(" years old ").append(user.getUserSex().toString()).append(" mazeltoving since ").append(Utils.getInstance().getDateAsString(context, user.getUserJoinDate() * 1000)).toString();
	}

	public String generateEventSubText(Event event) {
		return new StringBuilder().append("You have been added to ").append(event.getEventName()).append(event.getEventHoster() == null ? "" : " by " + event.getEventHoster().getMainText()).toString();
	}

	@Override
	public String getCoolText() {
		return getNotificationData().getCoolText();
	}

	@Override
	public Long getClickListenerId() {
		return getNotificationData().getClickListenerId();
	}

	@Override
	public Item getItem() {
		return null;
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
