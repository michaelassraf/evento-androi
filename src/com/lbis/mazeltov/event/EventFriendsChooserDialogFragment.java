package com.lbis.mazeltov.event;

import java.util.LinkedList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.User;
import com.lbis.model.view.AutoCompleteAdapter;
import com.lbis.model.view.UsersListAdapter;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.EventActions;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.actions.UserActions;
import com.lbis.utils.Enums;
import com.lbis.utils.Utils;

public class EventFriendsChooserDialogFragment extends EventDialogFragmentBase {

	ProgressDialog progressDialog;
	private UsersListAdapter adapter;
	private LinkedList<User> data;
	private SwipeListView eventChosedFriendsListView;
	private Button eventFriendsChooserCommit;
	private AutoCompleteTextView eventAutoCompleteView;
	private AutoCompleteAdapter<User, UserActions> eventUserAutoCompleteAdapter;

	public static EventFriendsChooserDialogFragment getDialogInstance() {
		return new EventFriendsChooserDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		FrameLayout main = (FrameLayout) inflater.inflate(R.layout.event_friends_choser_fragment, null);

		eventAutoCompleteView = (AutoCompleteTextView) main.findViewById(R.id.event_friends_chooser_auto_complete);

		data = new LinkedList<User>();

		adapter = new UsersListAdapter(getActivity(), data);

		eventUserAutoCompleteAdapter = new AutoCompleteAdapter<User, UserActions>(getActivity(), R.layout.event_list_view, new UserActions(), data, adapter);
		eventAutoCompleteView.setAdapter(eventUserAutoCompleteAdapter);

		eventChosedFriendsListView = (SwipeListView) main.findViewById(R.id.event_friends_chooser_list_view);
		eventFriendsChooserCommit = (Button) main.findViewById(R.id.event_friends_chooser_commit);

		eventChosedFriendsListView.setLayoutParams(new LinearLayout.LayoutParams(Utils.getInstance().getScreenSizes(getActivity())[0], Utils.getInstance().getScreenSizes(getActivity())[1]));

		eventChosedFriendsListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClickBackView(final int position) {
				super.onClickBackView(position);
				if (data != null && data.size() >= position) {

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							data.remove(position);
							adapter.setData(data);
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});

		eventFriendsChooserCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCurrentEditedEvent().setEventUsers(data);
				getCurrentEditedEvent().setEventOrganizer(new ExecuteManagementMethods().getTokenAndUserId(getActivity()).getUserId());
				class CreateEvent extends AsyncTask<Void, Void, Long> {

					@Override
					protected void onPreExecute() {
						progressDialog = Utils.getInstance().showProgressDialog(getActivity());
						super.onPreExecute();
					}

					@Override
					protected Long doInBackground(Void... params) {
						int numTries = 3;
						Long newEventId = null;
						while (true) {
							try {
								Event newEvent = new EventActions().createNewRequest(getCurrentEditedEvent(), WebReciever.getInstance().getConnection(), getActivity().getApplicationContext());
								newEventId = newEvent.getEventId();
								break;
							} catch (Exception e) {
								if (--numTries == 0) {
									log.error("Error while trying to create an event - ", e);
									return null;
								}
							}
						}

						return newEventId;
					}

					@Override
					protected void onPostExecute(Long result) {
						progressDialog.dismiss();
						if (result != null) {
							new ItemActions().putFile(getActivity().getApplicationContext(), result, Enums.ContentTypes.PNG, getCurrentEditedEvent().getEventPicture().getItemUrl());
						} else
							getMessageBar(getActivity()).show("Problem occur while to trying to create your event. Please try again later.");
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						ft.addToBackStack(EventFriendsChooserDialogFragment.class.getName());
						Utils.getInstance().removeDialogs((SherlockFragmentActivity) getActivity(), EventFriendsChooserDialogFragment.class.getName(), EventPictureChoserDialogFragment.class.getName(), EventBasicDetailsDialogFragment.class.getName());
						super.onPostExecute(result);
					}
				}
				new CreateEvent().execute();
			}
		});

		eventChosedFriendsListView.setAdapter(adapter);
		return main;
	}
}
