package com.lbis.media.camrecorder;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.Post;
import com.lbis.model.view.DialogFragmentBase;
import com.lbis.model.view.EventsListAdapter;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.actions.PostActions;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class SelectEventDialogFragment extends DialogFragmentBase {
	static SelectEventDialogFragment selectEventDialogFragment;
	ProgressDialog progressDialog;
	private ListView list;
	private EventsListAdapter adapter;
	private LinkedList<Event> data;
	private Button selectEventCancelButton;
	static String localPicturePath;
	static String localVideoPath;

	public static SelectEventDialogFragment getDialogInstance(String localPicturePath, String localVideoPath) {
		SelectEventDialogFragment.localPicturePath = localPicturePath;
		SelectEventDialogFragment.localVideoPath = localVideoPath;
		return new SelectEventDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FrameLayout main = (FrameLayout) inflater.inflate(R.layout.post_event_chooser_dialog_fragment, null);
		list = (ListView) main.findViewById(R.id.post_event_chooser_list_view);
		data = new EventDbExecutors().getAll(new Event(), getActivity());
		adapter = new EventsListAdapter(getActivity(), data, false);
		selectEventCancelButton = (Button) main.findViewById(R.id.post_event_chooser_cancel);
		selectEventCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Utils.getInstance().showDialog((SherlockFragmentActivity) getActivity(), SelectEventDialogFragment.class.getName(), ApprovePostDialogFragment.class.getName(), ApprovePostDialogFragment.getDialogInstance(localPicturePath, localVideoPath, data.get(arg2)));
				//
				// AlertDialog.Builder builderInner = new AlertDialog.Builder(
				// getActivity());
				// builderInner.setTitle("Share your moment in "
				// + eventSelected.getMainText());
				// final EditText editText = new EditText(getActivity());
				// editText.setGravity(Gravity.CENTER | Gravity.BOTTOM);
				// editText.setHint("Say something !");
				// ImageView imagePreview = new ImageView(getActivity());
				// Utils.getInstance().loadDesignedImageView(imagePreview,
				// localPicturePath, getActivity(), 3, 6, false, false,
				// true);
				// LinearLayout linearLayout = new LinearLayout(getActivity());
				// linearLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);
				// linearLayout.setOrientation(LinearLayout.VERTICAL);
				// LayoutParams params = new LayoutParams(
				// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// linearLayout.setLayoutParams(params);
				// linearLayout.addView(editText);
				// linearLayout.addView(imagePreview);
				// builderInner.setView(linearLayout);
				// builderInner.setPositiveButton("Share !",
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// class UploadMedia extends
				// AsyncTask<Void, Void, Boolean> {
				// @Override
				// protected Boolean doInBackground(
				// Void... params) {
				// try {
				// Post post = new PostActions().createNewRequest(
				// new Post(
				// eventSelected
				// .getId(),
				// editText.getText()
				// .toString(),
				// localVideoPath == null ? PostType.Image
				// : PostType.Video),
				// WebReciever.getInstance()
				// .getConnection(),
				// getActivity());
				// new ItemActions().putFile(
				// getActivity(),
				// post.getId(),
				// localVideoPath == null ? ContentTypes.JPG
				// : ContentTypes.MP4,
				// localVideoPath == null ? localPicturePath
				// : localVideoPath);
				// } catch (Throwable th) {
				// log.error(
				// "Problem uploading new post",
				// th);
				// }
				// return null;
				// }
				//
				// }
				// AsyncTaskMap.getAsyncHashMap().cancelAll();
				// UploadMedia task = new UploadMedia();
				// AsyncTaskMap.getAsyncHashMap().add(task);
				// task.execute();
				//
				// dialog.dismiss();
				// }
				// });
				// builderInner.show();

			}
		});

		return main;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

}
