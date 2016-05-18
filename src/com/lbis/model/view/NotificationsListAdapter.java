package com.lbis.model.view;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.Notification;
import com.lbis.model.Post;
import com.lbis.model.User;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;
import com.lbis.view.components.EventFragment;

public class NotificationsListAdapter extends ListAdapterAbs<Notification> {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	FragmentActivity activity;

	private static int WIDTH_SCALE = 4;
	private static int HEIGHT_SCALE = 8;

	public NotificationsListAdapter(FragmentActivity activity, LinkedList<Notification> data) {
		super(activity.getApplicationContext(), data);
		this.activity = activity;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View layout = convertView;
		if (convertView == null)
			layout = inflater.inflate(R.layout.notification_list_view, null);

		TextView notificationDetails = (TextView) layout.findViewById(R.id.notification_details);
		TextView notificationTitle = (TextView) layout.findViewById(R.id.notification_title);
		ImageView notificationImage = (ImageView) layout.findViewById(R.id.notification_picutre);

		notificationDetails.setText(data.get(position).getNotificationData() != null ? data.get(position).getNotificationData().getSubText() : "");

		notificationTitle.setText(data.get(position).getNotificationData() != null ? data.get(position).getNotificationData().getCoolText() : "");

		OnClickListener listener = null;

		switch (data.get(position).getNotificationTypeEnum()) {
		case Follow:
			final User user = data.get(position).getNotificationData();
			if (user == null)
				break;
			setPhoto(notificationImage, user.getPicturePath(), activity);
			listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						new ProfilePopUpDialog(activity, new User(user.getClickListenerId())).show();
					} catch (Exception e) {
						log.error("Can't parse user Id ", e);
					}
				}
			};
			break;
		case Post:
			final Post post = data.get(position).getNotificationData();
			if (post == null || post.getPostType() == null)
				break;
			switch (post.getPostType()) {
			case Image:
				Utils.getInstance().loadDesignedImageView(notificationImage, post.getPicturePath(), parent.getContext(), WIDTH_SCALE, HEIGHT_SCALE, true, false, false, PostType.Image, post.getItem().getFormattedUrl(), Utils.getInstance().getScreenSizes(activity)[0] / 20, 0);
				break;
			case Video:
				Utils.getInstance().loadDesignedImageView(notificationImage, post.getItem().getFormattedUrl().replace(".mp4", ".jpg"), parent.getContext(), WIDTH_SCALE, HEIGHT_SCALE, true, false, false, PostType.Video, post.getItem().getFormattedUrl(), Utils.getInstance().getScreenSizes(activity)[0] / 20, 0);

				break;
			}
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle args = new Bundle();
					args.putLong(EventFragment.EVENT_ID_IDENTIFER, post.getClickListenerId());
					EventFragment newfrag = new EventFragment();
					newfrag.setArguments(args);
					Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), newfrag);
				}
			};
			break;
		case Invitation:
			final Event event = data.get(position).getNotificationData();
			if (event == null)
				break;
			setPhoto(notificationImage, event.getPicturePath(), activity);
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle args = new Bundle();
					args.putLong(EventFragment.EVENT_ID_IDENTIFER, event.getClickListenerId());
					EventFragment newfrag = new EventFragment();
					newfrag.setArguments(args);
					Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), newfrag);
				}
			};
			break;
		}
		layout.setOnClickListener(listener);
		return layout;
	}

	private void setPhoto(ImageView imageView, String pictureUrl, Context context) {
		try {
			Utils.getInstance().loadDesignedImageView(imageView, pictureUrl, context, WIDTH_SCALE, HEIGHT_SCALE, false, false, false, PostType.Image, pictureUrl, Utils.getInstance().getScreenSizes(activity)[0] / 30, 0);
		} catch (Exception e) {
			Utils.getInstance().loadDesignedImageView(imageView, Enums.URLs.DefalutNoLoadedPicture.getValue(), context, WIDTH_SCALE, HEIGHT_SCALE, false, false, false, PostType.Image, pictureUrl, Utils.getInstance().getScreenSizes(activity)[0] / 30, 0);
			log.error("Error while trying to get picture url from event - ", e);
		}
	}
}
