package com.lbis.model.view;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;
import com.lbis.view.components.EventFragment;

public class EventsListAdapter extends ListAdapterAbs<Event> {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	FragmentActivity activity;
	boolean setOnClickListner;

	public EventsListAdapter(FragmentActivity activity, LinkedList<Event> data, boolean setOnClickListner) {
		super(activity.getApplicationContext(), data);
		this.activity = activity;
		this.setOnClickListner = setOnClickListner;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if (convertView == null)
			layout = inflater.inflate(R.layout.event_list_view, null);

		Event elvt = (Event) data.get(position);

		((TextView) layout.findViewById(R.id.event_title)).setText(elvt.getMainText());
		((TextView) layout.findViewById(R.id.event_details)).setText(elvt.getSubText());
		((TextView) layout.findViewById(R.id.time_to_event)).setText(elvt.getTimeToEvent());

		try {
			Utils.getInstance().loadDesignedImageView((ImageView) layout.findViewById(R.id.event_picutre), elvt.getPicturePath(), parent.getContext(), 3, 6, true, false, false, PostType.Image, null, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 20, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 50);
		} catch (Exception e) {
			Utils.getInstance().loadDesignedImageView((ImageView) layout.findViewById(R.id.event_picutre), Enums.URLs.DefalutNoLoadedPicture.getValue(), parent.getContext(), 3, 6);
			log.error("Error while trying to get picture url from event - ", e);
		}

		if (!setOnClickListner)
			return layout;

		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putLong(EventFragment.EVENT_ID_IDENTIFER, data.get(position).getEventId());
				EventFragment newfrag = new EventFragment();
				newfrag.setArguments(args);
				Utils.getInstance().replaceFragment(activity.getSupportFragmentManager(), newfrag);
			}
		});

		return layout;
	}
}
