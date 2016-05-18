package com.lbis.model.view;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.Post;
import com.lbis.model.User;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;
import com.lbis.view.components.EventFragment;

public class PostsListAdapter extends SectionListAdapterAbs<Post> {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Boolean showEventButton = true;

	public PostsListAdapter(FragmentActivity act, LinkedList<Post> data, boolean showEventButton) {
		super(act, data);
		this.showEventButton = showEventButton;
	}

	// Content View
	@Override
	public View getItemView(final int section, int position, View convertView, ViewGroup parent) {
		LinearLayout layout = null;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = (LinearLayout) inflator.inflate(R.layout.post_list_item, null);
		} else {
			layout = (LinearLayout) convertView;
		}

		ImageView imageView = (ImageView) layout.findViewById(R.id.post_list_content);
		try {
			switch (data.get(section).getPostType() == null ? Enums.PostType.Image : data.get(section).getPostType()) {
			case Video:
				String thumbnailLocation = new String(data.get(section).getContentListItem().getPicturePath().replaceAll(".mp4", ".jpg"));
				Utils.getInstance().loadDesignedImageView(imageView, thumbnailLocation, parent.getContext(), 1, 1, true, true, false, data.get(section).getPostType(), data.get(section).getContentListItem().getPicturePath(), Utils.getInstance().getScreenSizes(act)[0] / 10, Utils.getInstance().getScreenSizes(act)[0] / 100);
				break;
			case Image:
				Utils.getInstance().loadDesignedImageView(imageView, data.get(section).getContentListItem().getPicturePath(), parent.getContext(), 1, 1, true, true, false, PostType.Image, null, Utils.getInstance().getScreenSizes(act)[0] / 10, Utils.getInstance().getScreenSizes(act)[0] / 100);
				break;
			default:
				Utils.getInstance().loadDesignedImageView(imageView, data.get(section).getContentListItem().getPicturePath(), parent.getContext(), 1, 1);
				break;
			}

		} catch (Exception e) {
			log.error("Error while trying to get picture url from post - ", e);
		}

		((TextView) layout.findViewById(R.id.post_time)).setText(data.get(section).getTimeFromPost());
		if (showEventButton) {
			((TextView) layout.findViewById(R.id.post_event_button)).setText("Watch " + data.get(section).getPostEventName());
			((TextView) layout.findViewById(R.id.post_event_button)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle args = new Bundle();
					args.putLong(EventFragment.EVENT_ID_IDENTIFER, data.get(section).getPostEventId());
					EventFragment newfrag = new EventFragment();
					newfrag.setArguments(args);
					Utils.getInstance().replaceFragment(act.getSupportFragmentManager(), newfrag);
				}
			});
		}
		return layout;
	}

	@Override
	public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
		LinearLayout layout = null;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		((TextView) layout.findViewById(R.id.profile_name)).setText(data.get(section).getHeaderListItem().getSubText());
		((TextView) layout.findViewById(R.id.header_sub_text)).setText(data.get(section).getHeaderListItem().getMainText());

		ImageView imageView = (ImageView) layout.findViewById(R.id.profile_image);
		if (Utils.getInstance().getScreenSizes(act)[0] < Utils.getInstance().getScreenSizes(act)[1]) {
			Utils.getInstance().loadDesignedImageView(imageView, data.get(section).getHeaderListItem().getPicturePath().toString(), parent.getContext(), 5, 10, false, true, false, PostType.Image, null, Utils.getInstance().getScreenSizes(act)[0] / 20, Utils.getInstance().getScreenSizes(act)[0] / 100);
		} else {
			Utils.getInstance().loadDesignedImageView(imageView, data.get(section).getHeaderListItem().getPicturePath().toString(), parent.getContext(), 10, 5, false, true, false, PostType.Image, null, Utils.getInstance().getScreenSizes(act)[0] / 20, Utils.getInstance().getScreenSizes(act)[0] / 100);
		}
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Long userId = data.get(section).getPostPoster() == null ? 0L : data.get(section).getPostPoster().getId();
					new ProfilePopUpDialog(act, new User(userId)).show();
				} catch (Exception e) {
					log.error("Can't parse user Id " + data.get(section).getPostPoster() == null ? "0" : data.get(section).getPostPoster());
				}
			}
		});

		return layout;
	}
}
