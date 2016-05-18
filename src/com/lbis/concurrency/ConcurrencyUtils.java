package com.lbis.concurrency;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import android.content.Context;

import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.database.executors.model.FollowDbExecutors;
import com.lbis.database.executors.model.NotificationDbExecutors;
import com.lbis.database.executors.model.PostsDbExecutors;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.EventActions;
import com.lbis.server.actions.FollowActions;
import com.lbis.server.actions.NotificationActions;
import com.lbis.server.actions.PostActions;

public class ConcurrencyUtils {
	Logger log = Logger.getLogger(getClass().getSimpleName());
	private static ConcurrencyUtils concurrencyUtils;

	public static ConcurrencyUtils getInstance() {
		if (concurrencyUtils == null)
			concurrencyUtils = new ConcurrencyUtils();

		return concurrencyUtils;
	}

	public String updateAllCache(final long since, final Context ctx) {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Runnable postsCache = new Runnable() {
			@Override
			public void run() {
				ConcurrencyUtils.getInstance().updatePostsCache(since, ctx);
			}
		};
		executor.execute(postsCache);

		Runnable eventsCache = new Runnable() {
			@Override
			public void run() {
				ConcurrencyUtils.getInstance().updateEventsCache(since, ctx);
			}
		};
		executor.execute(eventsCache);

		Runnable notificationCache = new Runnable() {
			@Override
			public void run() {
				ConcurrencyUtils.getInstance().updateNotificationsCache(since, ctx);
			}
		};

		executor.execute(notificationCache);

		Runnable followsCache = new Runnable() {
			@Override
			public void run() {
				ConcurrencyUtils.getInstance().updateFollowsCache(ctx);
			}
		};

		executor.execute(followsCache);

		executor.shutdown();
		return "Launched all threads";
	}

	public void updateEventsCache(long since, Context context) {
		try {
			log.info("Starting to update events cache");
			new EventDbExecutors().putAll(context, new EventActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), context));
			log.info("Successfully updated events cache");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("Error updating events cache - ", e);
		} catch (Exception e) {
			log.error("Error updating events cache - ", e);
		}
	}

	public void updatePostsCache(long since, Context context) {
		try {
			log.info("Starting to update posts cache");
			new PostsDbExecutors().putAll(context, new PostActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), context));
			log.info("Successfully updated posts cache");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("Error updating posts cache - ", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error updating posts cache - ", e);
		}
	}

	public void updateNotificationsCache(long since, Context context) {
		try {
			log.info("Starting to notifications posts cache");
			new NotificationDbExecutors().putAll(context, new NotificationActions().getAllObjectsInit(WebReciever.getInstance().getConnection(), context));
			log.info("Successfully updated notifications cache");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("Error updating notifications cache - ", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error updating notifications cache - ", e);
		}
	}

	public void updateFollowsCache(Context context) {
		try {
			log.info("Starting to follows posts cache");
			new FollowDbExecutors().putAll(context, new FollowActions().getAllMyFollowers(WebReciever.getInstance().getConnection(), context));
			log.info("Successfully updated follows cache");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("Error updating follows cache - ", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error updating follows cache - ", e);
		}
	}

}
