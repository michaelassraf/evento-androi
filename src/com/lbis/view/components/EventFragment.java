package com.lbis.view.components;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltotrfresh.myviews.PinnedPullToRefreshListView;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.concurrency.EventUpdateCallable;
import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.Post;
import com.lbis.model.User;
import com.lbis.model.view.PostsListAdapter;
import com.lbis.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class EventFragment extends Fragment implements AbsListView.OnScrollListener, OnRefreshListener<ListView> {

	public static final String EVENT_ID_IDENTIFER = "event_id";

	PostsListAdapter eventPostsAdapter;
	PinnedPullToRefreshListView eventListView;
	ListView eventListRefreshableView;
	ImageView eventPictureView;
	TextView eventNameView;
	FrameLayout eventNameHolderView;
	LinearLayout eventDetailsView;
	ProfilesLayout eventOrganizerView;
	ProfilesLayout eventGuestsView;
	InfoLayout eventWhatView;
	InfoLayout eventWhereView;
	InfoLayout eventWhenView;

	Event event;
	Long eventId;

	// params for the quick return trick
	QuickReturnState state = QuickReturnState.ON_SCREEN;
	int lastY;
	int lastFirstChild;
	ObjectAnimator quickReturnBarHideAnimator;
	ObjectAnimator quickReturnBarReturnAnimator;
	Logger log = Logger.getLogger(getClass().getSimpleName());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.event_fragment, container, false);
		View headerView = inflater.inflate(R.layout.event_header, null);

		eventNameView = (TextView) rootView.findViewById(R.id.event_sticky_text);
		eventListView = (PinnedPullToRefreshListView) rootView.findViewById(R.id.event_list_view);
		eventPictureView = (ImageView) headerView.findViewById(R.id.event_header_background);
		eventNameHolderView = (FrameLayout) rootView.findViewById(R.id.event_sticky_text_holder);
		eventListRefreshableView = eventListView.getRefreshableView();
		eventDetailsView = (LinearLayout) headerView.findViewById(R.id.event_content);

		if (getArguments() != null && getArguments().getLong(EVENT_ID_IDENTIFER) > 0 && eventId == null)
			eventId = getArguments().getLong(EVENT_ID_IDENTIFER);
		else
			getActivity().getSupportFragmentManager().popBackStack();

		log.info("Event to be displayed is " + eventId);

		eventPictureView.setLayoutParams(new LinearLayout.LayoutParams(Utils.getInstance().getScreenSizes(getActivity())[0], Utils.getInstance().getScreenSizes(getActivity())[1]));

		FrameLayout.LayoutParams paramas = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, Utils.getInstance().getScreenSizes(getActivity())[1] / 7);
		paramas.gravity = Gravity.BOTTOM;
		eventNameHolderView.setLayoutParams(paramas);

		eventListView.getRefreshableView().addHeaderView(headerView);
		eventPostsAdapter = new PostsListAdapter(getActivity(), new LinkedList<Post>(), false);
		eventListView.setAdapter(eventPostsAdapter);

		eventListView.setOnRefreshListener(this);

		eventListRefreshableView.setOnScrollListener(this);

		lastFirstChild = eventListRefreshableView.getFirstVisiblePosition();
		final View firstChild = eventListRefreshableView.getChildAt(lastFirstChild);
		lastY = firstChild == null ? 0 : firstChild.getTop();
		View quickReturnBar = eventNameHolderView;
		quickReturnBarReturnAnimator = ObjectAnimator.ofFloat(quickReturnBar, "translationY", 0);
		quickReturnBarReturnAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				state = QuickReturnState.RETURNING;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				state = QuickReturnState.ON_SCREEN;
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				state = QuickReturnState.OFF_SCREEN;
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		quickReturnBarHideAnimator = ObjectAnimator.ofFloat(quickReturnBar, "translationY", Utils.getInstance().getScreenSizes(getActivity())[1] / 7);
		quickReturnBarHideAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				state = QuickReturnState.HIDING;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				state = QuickReturnState.OFF_SCREEN;
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				state = QuickReturnState.ON_SCREEN;
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});

		AsyncTaskMap.getAsyncHashMap().cancelAll();
		GetEventFromDB task = new GetEventFromDB();
		AsyncTaskMap.getAsyncHashMap().add(task);
		task.execute();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				quickReturnBarReturnAnimator.start();
			}
		}, 100);
	}

	@Override
	public void onScroll(AbsListView list_view, int first_visible_child, int _, int __) {
		final View first_child = list_view.getChildAt(first_visible_child);
		int y_position = first_child == null ? 0 : first_child.getTop() - eventPictureView.getHeight();

		switch (state) {
		case OFF_SCREEN:
			if (!quickReturnBarIsReturning() && (first_visible_child == lastFirstChild && y_position > lastY) || first_visible_child < lastFirstChild)
				quickReturnBarReturnAnimator.start();
			break;

		case ON_SCREEN:
			if (!quickReturnBarIsGoingAway() && (first_visible_child == lastFirstChild && y_position < lastY) || first_visible_child > lastFirstChild)
				quickReturnBarHideAnimator.start();
			break;

		case RETURNING:
			if ((first_visible_child == lastFirstChild && y_position < lastY) || first_visible_child > lastFirstChild) {
				quickReturnBarReturnAnimator.cancel();
				quickReturnBarHideAnimator.start();
			}
			break;

		case HIDING:
			if ((first_visible_child == lastFirstChild && y_position > lastY) || first_visible_child < lastFirstChild) {
				quickReturnBarHideAnimator.cancel();
				quickReturnBarReturnAnimator.start();
			}
			break;
		}
		lastFirstChild = first_visible_child;
		lastY = y_position;
	}

	@Override
	public void onScrollStateChanged(AbsListView _, int __) {
	}

	private boolean quickReturnBarIsReturning() {
		return quickReturnBarReturnAnimator.isRunning();
	}

	private boolean quickReturnBarIsGoingAway() {
		return quickReturnBarHideAnimator.isRunning() || quickReturnBarHideAnimator.isStarted();
	}

	private static enum QuickReturnState {
		ON_SCREEN, OFF_SCREEN, RETURNING, HIDING
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		AsyncTaskMap.getAsyncHashMap().cancelAll();
		GetEventFromDB task = new GetEventFromDB();
		AsyncTaskMap.getAsyncHashMap().add(task);
		task.execute();
	}

	private class GetEventFromDB extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			if (getActivity() != null)
				event = new EventDbExecutors().get(eventId, getActivity().getApplicationContext());
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (getActivity() != null)
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							if (checkEventFragmentIsFine()) {
								displayEvent(event);
								eventPostsAdapter.setData(event.getEventPosts());
								eventPostsAdapter.notifyDataSetChanged();
								eventListView.onRefreshComplete();
							}
						} catch (Exception ex) {
							log.error("Error while trying to display evend with id" + eventId, ex);
						}
					}
				});
			GetEventFromWeb task = new GetEventFromWeb();
			AsyncTaskMap.getAsyncHashMap().add(task);
			task.execute();
			super.onPostExecute(result);
		}
	}

	class GetEventFromWeb extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			ExecutorService pool = Executors.newFixedThreadPool(1);
			EventUpdateCallable postsUpdate = new EventUpdateCallable(getActivity(), eventId);
			Future<Boolean> future = pool.submit(postsUpdate);
			Boolean isSuccess = false;
			try {
				isSuccess = future.get();
			} catch (Exception e) {
				log.error("Cannot get result from thread", e);
			}
			event = new EventDbExecutors().get(eventId, getActivity());
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (getActivity() != null)
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							if (checkEventFragmentIsFine()) {
								displayEvent(event);
								eventPostsAdapter.setData(event.getEventPosts());
								eventPostsAdapter.notifyDataSetChanged();
								eventListView.onRefreshComplete();
							}
						} catch (Exception ex) {
							log.error("Error while trying to display evend with id" + eventId, ex);
						}
					}
				});
			super.onPostExecute(result);
		}
	}

	public boolean checkEventFragmentIsFine() {
		if (event != null && eventPostsAdapter != null && eventListView != null)
			return true;
		return false;
	}

	public void displayEvent(Event event) throws UnsupportedEncodingException, Exception {
		eventPostsAdapter.setData(event.getEventPosts());
		final LinkedList<User> orgnizerLinkedList = new LinkedList<User>();
		orgnizerLinkedList.add(event.getEventHoster());
		final LinkedList<User> guestsLinkedList = new LinkedList<User>();
		if (event.getEventUsers() != null)
			for (int i = 0; i < 5 && i < event.getEventUsers().size(); i++) {
				if (!event.getEventUsers().get(i).getId().equals(event.getEventOrganizer())) {
					guestsLinkedList.add(event.getEventUsers().get(i));
				}
			}
		Utils.getInstance().loadDesignedImageView(eventPictureView, event.getEventPicture() == null ? "" : event.getEventPicture().getFormattedUrl(), getActivity().getApplicationContext(), 1, 1);
		eventNameView.setText(event.getEventName());
		eventDetailsView.removeAllViews();
		eventWhatView = new InfoLayout(getActivity().getApplicationContext(), "What", event.getEventDescription());
		eventDetailsView.addView(eventWhatView);

		eventWhereView = new InfoLayout(getActivity().getApplicationContext(), "Where", event.getEventLocation() == null ? "" : event.getEventLocation());
		eventDetailsView.addView(eventWhereView);

		eventWhenView = new InfoLayout(getActivity().getApplicationContext(), "When", Utils.getInstance().getDateAndHourAsString(getActivity(), event.getEventTime() * 1000));
		eventDetailsView.addView(eventWhenView);

		eventOrganizerView = new ProfilesLayout("Who", orgnizerLinkedList, getActivity());
		eventDetailsView.addView(eventOrganizerView);

		eventGuestsView = new ProfilesLayout("Whom", guestsLinkedList, getActivity());
		eventDetailsView.addView(eventGuestsView);
	}

}
