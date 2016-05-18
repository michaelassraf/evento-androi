package com.lbis.media.camrecorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.mazeltov.BaseActivity;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.Post;
import com.lbis.model.view.DialogFragmentBase;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.actions.PostActions;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class ApprovePostDialogFragment extends DialogFragmentBase {
	static ApprovePostDialogFragment selectEventDialogFragment;
	TextView postApproveTitleTextView;
	EditText postApproveEditText;
	ImageView postApproveImageView;
	Button postApproveCommit;
	static String localPicturePath;
	static String localVideoPath;
	static Event selectedEvent;
	static Activity activity;

	public static ApprovePostDialogFragment getDialogInstance(String localPicturePath, String localVideoPath, Event selectedEvent) {
		ApprovePostDialogFragment.localPicturePath = localPicturePath;
		ApprovePostDialogFragment.localVideoPath = localVideoPath;
		ApprovePostDialogFragment.selectedEvent = selectedEvent;
		return new ApprovePostDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FrameLayout main = (FrameLayout) inflater.inflate(R.layout.post_approve_dialog_fragment, null);
		postApproveTitleTextView = (TextView) main.findViewById(R.id.post_approve_title);
		postApproveCommit = (Button) main.findViewById(R.id.post_approve_commit);
		postApproveEditText = (EditText) main.findViewById(R.id.post_approve_edit_text);
		postApproveImageView = (ImageView) main.findViewById(R.id.post_approve_image_view);

		postApproveTitleTextView.setText("Share you moment in " + selectedEvent.getEventName() + " !");

		Utils.getInstance().loadDesignedImageView(postApproveImageView, localPicturePath, getActivity(), 2, 4, false, false, true, PostType.Image, null, Utils.getInstance().getScreenSizes(getActivity())[0] / 20, Utils.getInstance().getScreenSizes(getActivity())[0] / 50);
		BaseActivity.saveMyAcitivity(getActivity());

		postApproveCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						try {
							Post post = new PostActions().createNewRequest(new Post(selectedEvent.getId(), postApproveEditText.getText().toString(), localVideoPath == null ? PostType.Image : PostType.Video), WebReciever.getInstance().getConnection(), BaseActivity.getMyActivity());
							new ItemActions().putFile(BaseActivity.getMyActivity(), post.getId(), localVideoPath == null ? ContentTypes.JPG : ContentTypes.MP4, localVideoPath == null ? localPicturePath : localVideoPath);
						} catch (Throwable th) {
							log.error("Problem uploading new post", th);
						}
					}
				}.start();

				Utils.getInstance().removeDialogs((SherlockFragmentActivity) getDialog().getOwnerActivity(), ApprovePostDialogFragment.class.getName(), SelectEventDialogFragment.class.getName());
			}
		});
		return main;
	}

}
