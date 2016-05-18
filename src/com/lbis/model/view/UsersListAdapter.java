package com.lbis.model.view;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.model.User;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class UsersListAdapter extends ListAdapterAbs<User> {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	FragmentActivity activity;

	public UsersListAdapter(FragmentActivity activity, LinkedList<User> data) {
		super(activity.getApplicationContext(), data);
		this.activity = activity;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if (convertView == null)
			layout = inflater.inflate(R.layout.user_list_view, null);

		User currentObject = (User) data.get(position);

		((TextView) layout.findViewById(R.id.user_title)).setText(currentObject.getMainText());
		((TextView) layout.findViewById(R.id.user_details)).setText(currentObject.getAddedText());

		try {
			Utils.getInstance().loadDesignedImageView((ImageView) layout.findViewById(R.id.user_picutre), currentObject.getPicturePath(), parent.getContext(), 3, 6, false, false, false, PostType.Image, null, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 30, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 30);
		} catch (Exception e) {
			log.error("Error while trying to get picture url from user - ", e);
		}

		((ImageView) layout.findViewById(R.id.user_picutre)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProfilePopUpDialog(activity, data.get(position)).show();
			}
		});

		return layout;
	}
}
