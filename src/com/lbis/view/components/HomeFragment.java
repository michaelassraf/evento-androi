package com.lbis.view.components;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltotrfresh.myviews.PinnedPullToRefreshListView;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.concurrency.PostsUpdateCallable;
import com.lbis.database.executors.model.PostsDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Post;
import com.lbis.model.view.PostsListAdapter;
import com.lbis.utils.Utils;

public class HomeFragment extends Fragment implements OnRefreshListener<ListView> {

	public static String IS_FIRST_RUN = "is_first_run";
	private PinnedPullToRefreshListView mPullRefreshListView;
	private PostsListAdapter postsAdapter;
	LinkedList<Post> list;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Boolean stopRefresh = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		LinearLayout main = (LinearLayout) inflater.inflate(R.layout.home_fragment, null);
		mPullRefreshListView = (PinnedPullToRefreshListView) main.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.getRefreshableView();

		list = new PostsDbExecutors().getAll(new Post(), getActivity().getApplicationContext());

		postsAdapter = new PostsListAdapter(getActivity(), list, true);

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// new GetOldPosts().execute();
			}
		});

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, Utils.getInstance().getScreenSizes(getActivity())[1]);

		mPullRefreshListView.setLayoutParams(params);
		mPullRefreshListView.setAdapter(postsAdapter);
		Bundle arguments = getArguments();

		if (arguments != null) {
			stopRefresh = arguments.get(IS_FIRST_RUN) == null ? true : !(Boolean) arguments.get(IS_FIRST_RUN);
			if (!stopRefresh) {
				log.info("Start home screen refresh");
				mPullRefreshListView.setRefreshing();
				AsyncTaskMap.getAsyncHashMap().cancelAll();
				GetNewPostFromDB task = new GetNewPostFromDB();
				AsyncTaskMap.getAsyncHashMap().add(task);
				task.execute();
				this.getArguments().remove(IS_FIRST_RUN);
			}
		}

		return main;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		log.info("Start home screen refresh");
		AsyncTaskMap.getAsyncHashMap().cancelAll();
		GetNewPostFromDB task = new GetNewPostFromDB();
		AsyncTaskMap.getAsyncHashMap().add(task);
		task.execute();
	}

	private class GetNewPostFromDB extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				list = new PostsDbExecutors().getAll(new Post(), getActivity().getApplicationContext());
				postsAdapter.setData(list);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				stopRefreshingAndUpdateData();
				log.info("New post asyntack was interrupted");
			} catch (Exception e) {
				stopRefreshingAndUpdateData();
				log.error("Failed loading posts from DB", e);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (stopRefresh) {
				stopRefreshingAndUpdateData();
			}
			stopRefresh = !stopRefresh;
			GetNewPostFromWeb task = new GetNewPostFromWeb();
			task.execute();
			AsyncTaskMap.getAsyncHashMap().add(task);
			super.onPostExecute(result);
		}
	}

	private class GetNewPostFromWeb extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			ExecutorService pool = Executors.newFixedThreadPool(1);
			PostsUpdateCallable postsUpdate = new PostsUpdateCallable(getActivity());
			Future<Boolean> future = pool.submit(postsUpdate);
			Boolean isSuccess = false;
			try {
				isSuccess = future.get();
			} catch (Exception e) {
				stopRefreshingAndUpdateData();
				log.error("Cannot get result from thread", e);
			}
			if (getActivity() != null) {
				list = new PostsDbExecutors().getAll(new Post(), getActivity().getApplicationContext());
				postsAdapter.setData(list);
			}
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			stopRefreshingAndUpdateData();
			super.onPostExecute(result);
		}
	}

	public void stopRefreshingAndUpdateData() {
		if (getActivity() != null)
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (postsAdapter != null && mPullRefreshListView != null) {
						postsAdapter.notifyDataSetChanged();
						mPullRefreshListView.onRefreshComplete();
						log.info("Release home screen refresh");
					}
				}
			});

	}
}
