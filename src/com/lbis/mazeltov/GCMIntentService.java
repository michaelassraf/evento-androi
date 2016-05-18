package com.lbis.mazeltov;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.lbis.database.executors.model.NotificationToInformDbExecutors;
import com.lbis.model.Notification;
import com.lbis.notifications.RegisterGCMOnServer;
import com.lbis.utils.Enums;
import com.lbis.utils.GsonSerializer;

public class GCMIntentService extends GCMBaseIntentService {
	final static Logger log = Logger.getLogger(GCMIntentService.class.getSimpleName());
	public final static String GO_TO_FUCKING_USER = "go_to_mother_fucking_user";
	public final static String GO_TO_FUCKING_POST = "go_to_mother_fucking_post";
	public final static String GO_TO_FUCKING_EVENT = "go_to_mother_fucking_event";

	public GCMIntentService() {
		super(Enums.GCM.GCMSenderId.getValue());
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		log.info("Device registered in crappy GCM : regId = " + registrationId);
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			new RegisterGCMOnServer(context).execute(registrationId);
		}
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		log.info("Device unregistered from crappy GCM");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
		} else {
			log.info("Ignoring unregister callback from GCM");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		log.info("message recoieved from crappy GCM");
		String message = intent.getStringExtra("data");
		generateNotification(context, message);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		log.info("message deleted from crappy GCM");
	}

	@Override
	public void onError(Context context, String errorId) {
		log.info("Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		log.info("Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private void generateNotification(Context context, String message) {
		log.info("GCM messgae successfully recieved - " + message);
		com.lbis.model.Notification notificationFromServer = null;
		try {
			notificationFromServer = GsonSerializer.getInstance().fromJson(message, Notification.class);
			if (notificationFromServer == null || notificationFromServer.getNotificationData() == null || notificationFromServer.getPicturePath() == null || notificationFromServer.getNotificationData().getCoolText() == null || notificationFromServer.getNotificationData().getSubText() == null)
				throw new Exception("The fucking object was empty after parsing ");
			log.info("Successfully parsed notification to object");
		} catch (Exception ex) {
			log.error("Error while trying to parse notificaiton object ", ex);
			return;
		}

		new NotificationToInformDbExecutors().put(getApplicationContext(), notificationFromServer);
		String url = notificationFromServer.getPicturePath();
		url = url.replace(".mp4", ".jpg");
		Bitmap notificationPicutre = getBitmapFromURL(url);
		NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
		notiStyle.setBigContentTitle(notificationFromServer.getSubText());
		notiStyle.setSummaryText(notificationFromServer.getNotificationData().getCoolText());
		notiStyle.bigPicture(notificationPicutre);
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		android.app.Notification myNotification = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.mazeltov_logo).setAutoCancel(true).setLargeIcon(notificationPicutre).setContentIntent(resultPendingIntent).setContentTitle(notificationFromServer.getNotificationData().getSubText()).setContentText(notificationFromServer.getNotificationData().getCoolText()).setLights(0xFFFF0000, 1000, 5000).setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.FLAG_SHOW_LIGHTS).setStyle(notiStyle).build();
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.notify(300, myNotification);
	}

	public Bitmap getBitmapFromURL(String strURL) {
		try {
			URL url = new URL(strURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			log.error("Error when trying to download ", e);
			return null;
		}
	}

}
