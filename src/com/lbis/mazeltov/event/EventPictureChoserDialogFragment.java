package com.lbis.mazeltov.event;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.mazeltov.R;
import com.lbis.model.Item;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.PictureDownloader;
import com.lbis.utils.Utils;

public class EventPictureChoserDialogFragment extends EventDialogFragmentBase {

	private static int SELECT_EVENT_PICTURE = 2;
	private static String SELECT_EVENT_PICTURE_TITLE = "Select the event picture";

	Button eventImageCommit;
	ImageView eventImageView;

	public static EventPictureChoserDialogFragment getDialogInstance() {
		return new EventPictureChoserDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		FrameLayout main = (FrameLayout) inflater.inflate(R.layout.event_picture_choser_fragment, null);
		int[] screenSizes = Utils.getInstance().getScreenSizes(getActivity());
		eventImageCommit = (Button) main.findViewById(R.id.event_creation_picture_button);
		eventImageCommit.setText(getCurrentEditedEvent().getEventName() != null ? getCurrentEditedEvent().getEventName() : "");
		eventImageCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getCurrentEditedEvent().getEventPicture() != null && getCurrentEditedEvent().getEventPicture().getItemUrl() != null) {

					Utils.getInstance().showDialog((SherlockFragmentActivity) getActivity(), EventPictureChoserDialogFragment.class.getName(), EventFriendsChooserDialogFragment.class.getName(), EventFriendsChooserDialogFragment.getDialogInstance());
				} else
					getMessageBar(getActivity()).show(SELECT_EVENT_PICTURE_TITLE);

			}
		});

		eventImageView = (ImageView) main.findViewById(R.id.event_creation_picture);
		eventImageView.setLayoutParams(new FrameLayout.LayoutParams(screenSizes[0], screenSizes[1] / 2));
		eventImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.getInstance().launchImageChoser(getActivity(), SELECT_EVENT_PICTURE_TITLE, SELECT_EVENT_PICTURE);

			}
		});

		return main;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_EVENT_PICTURE) {
				try {
					Uri selectedImageUri = data.getData();
					String path = PictureDownloader.getPath(getActivity(), selectedImageUri);
					getCurrentEditedEvent().setEventPicture(new Item(null, path, ContentTypes.PNG, null));
					Utils.getInstance().loadDesignedImageView(eventImageView, getCurrentEditedEvent().getEventPicture().getItemUrl(), getActivity(), 1, 2, false, false, true, PostType.Image, null, Utils.getInstance().getScreenSizes(getActivity())[0] / 10, Utils.getInstance().getScreenSizes(getActivity())[0] / 15);

				} catch (Exception e) {
					log.error("Bad content recieved from the file picker");
				}
			}
		}
	}

}
