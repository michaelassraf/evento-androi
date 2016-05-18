/*
 * Copyright 2012 Roman Nurik + Nick Butcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.concurrency.NotificationsUpdateCallable;
import com.lbis.database.executors.model.NotificationDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Notification;
import com.lbis.model.view.NotificationsListAdapter;
import com.lbis.utils.Utils;

public class NotificationsFragment extends Fragment implements OnRefreshListener<ListView> {

	NotificationsListAdapter adapter;
	PullToRefreshListView list;
	LinkedList<Notification> data;
	Logger log = Logger.getLogger(getClass().getSimpleName());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.notifications, container, false);

		list = (PullToRefreshListView) rootView.findViewById(R.id.notification_list);
		list.setOnRefreshListener(this);
		list.getRefreshableView();

		data = new NotificationDbExecutors().getAll(new Notification(), getActivity().getApplicationContext());

		adapter = new NotificationsListAdapter(getActivity(), data);

		list.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// new GetOldPosts().execute();
			}
		});

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, Utils.getInstance().getScreenSizes(getActivity())[1]);
		list.setLayoutParams(params);
		list.setAdapter(adapter);
		list.setRefreshing();
		AsyncTaskMap.getAsyncHashMap().cancelAll();
		GetNewNotificationsFromDB task = new GetNewNotificationsFromDB();
		AsyncTaskMap.getAsyncHashMap().add(task);
		task.execute();
		return rootView;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		AsyncTaskMap.getAsyncHashMap().cancelAll();
		GetNewNotificationsFromDB task = new GetNewNotificationsFromDB();
		AsyncTaskMap.getAsyncHashMap().add(task);
		task.execute();
	}

	private class GetNewNotificationsFromDB extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				data = new NotificationDbExecutors().getAll(new Notification(), getActivity().getApplicationContext());
				adapter.setData(data);
			} catch (Exception e) {
				log.error("Failed loading events from DB", e);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			list.onRefreshComplete();
			adapter.notifyDataSetChanged();
			GetNewNotificationsFromWeb task = new GetNewNotificationsFromWeb();
			task.execute();
			AsyncTaskMap.getAsyncHashMap().add(task);
			super.onPostExecute(result);
		}
	}

	private class GetNewNotificationsFromWeb extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			ExecutorService pool = Executors.newFixedThreadPool(1);
			NotificationsUpdateCallable postsUpdate = new NotificationsUpdateCallable(getActivity());
			Future<Boolean> future = pool.submit(postsUpdate);
			Boolean isSuccess = false;
			try {
				isSuccess = future.get();
			} catch (Exception e) {
				log.error("Cannot get result from thread", e);
			}
			if (getActivity() != null) {
				data = new NotificationDbExecutors().getAll(new Notification(), getActivity().getApplicationContext());
				adapter.setData(data);
			}

			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result && adapter != null && list != null) {
				adapter.notifyDataSetChanged();
				list.onRefreshComplete();
			}
			super.onPostExecute(result);
		}
	}

}
