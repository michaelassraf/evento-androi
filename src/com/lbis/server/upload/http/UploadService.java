package com.lbis.server.upload.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.lbis.mazeltov.R;
import com.lbis.model.Item;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.objects.model.ItemServerWrapper;
import com.lbis.server.upload.http.FileMultipartEntity.ProgressListener;
import com.lbis.utils.Enums.ContentTypes;

public class UploadService extends Service {

	public static final String ARG_FILE_PATH = "file_path";
	public static final String ARG_CONTENT_TYPE = "file_content_type";
	public static final String ARG_CONNECTED_ID = "file_connected_id";
	public static final String UPLOAD_STATE_CHANGED_ACTION = "UPLOAD_STATE_CHANGED_ACTION";
	public static final String UPLOAD_CANCELLED_ACTION = "UPLOAD_CANCELLED_ACTION";
	public static final String PERCENT_EXTRA = "percent";
	public static final String MSG_EXTRA = "msg";

	private static final String FILE_POST_ATTRIBUTE = "file";
	private static final String FILE_DATA_POST_ATTRIBUTE = "jsonData";

	private static final int NOTIFY_ID_UPLOAD = 1337;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	long totalSize;
	String filePath;
	DefaultHttpClient connection;
	private NotificationManager nm;
	static Thread threadInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		IntentFilter f = new IntentFilter();
		f.addAction(UPLOAD_CANCELLED_ACTION);
		registerReceiver(uploadCancelReceiver, f);
		BasicHttpParams httpParamas = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParamas, 3600000);
		HttpConnectionParams.setSoTimeout(httpParamas, 3600000);
		HttpConnectionParams.setSocketBufferSize(httpParamas, 1024);
		HttpConnectionParams.setTcpNoDelay(httpParamas, true);
		HttpConnectionParams.setTcpNoDelay(httpParamas, true);
		HttpConnectionParams.setStaleCheckingEnabled(httpParamas, false);
		connection = new DefaultHttpClient(httpParamas);
		log.info("Upload service is warming up !");
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					ContentTypes contentType = null;
					Long connectedId = null;
					filePath = intent.getStringExtra(ARG_FILE_PATH);
					int contentTypeIndex = intent.getIntExtra(ARG_CONTENT_TYPE, 0);
					contentType = ContentTypes.values()[contentTypeIndex];
					connectedId = intent.getLongExtra(ARG_CONNECTED_ID, 0);
					filePath.replace("file://", "");
					log.info("Params for the upload seesion are - connectend id  " + connectedId + " content type " + contentType.toString() + " file path " + filePath);
					ItemActions itemActions = new ItemActions();
					final String msg = "mazeltoving...";
					String verbalResponse = null;
					StringEntity requestEntity = itemActions.makeJsonRequest(new Item(contentType, connectedId), getApplicationContext());
					HttpPost postMethod = new HttpPost(itemActions.createNewRequestUrl().getValue());
					postMethod.setEntity(requestEntity);

					File fileToUpload = new File(filePath);
					FileMultipartEntity multipartContent = new FileMultipartEntity(new ProgressListener() {
						@Override
						public void transferred(long num) {
							int percentComplete = (int) ((num / (float) totalSize) * 100);
							log.info("Thread name : " + Thread.currentThread().getName() + " file " + filePath + " is uploading... progress is :" + num + " of :" + totalSize);
							if (percentComplete % 10 == 0) {
								Notification notification = buildNotification(msg + " " + percentComplete + "%", percentComplete);
								nm.notify(NOTIFY_ID_UPLOAD, notification);
							}
						}
					});
					multipartContent.addPart(FILE_POST_ATTRIBUTE, new FileBody(fileToUpload));
					multipartContent.addPart(FILE_DATA_POST_ATTRIBUTE, new StringBody(itemActions.makeStringJsonRequest(new Item(contentType, connectedId), getApplicationContext())));
					totalSize = multipartContent.getContentLength();
					postMethod.setEntity(multipartContent);
					log.info("Thread name : " + Thread.currentThread().getName() + " Starting to upload, total size in bytes : " + totalSize);
					long startTime = System.currentTimeMillis();
					HttpResponse response = connection.execute(postMethod);
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
						verbalResponse = "Server is down";
					else
						log.info("Thread name : " + Thread.currentThread().getName() + " " + totalSize + " bytes uploaded in " + (System.currentTimeMillis() - startTime) + " ms");

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					verbalResponse = out.toString();
					log.info("Thread name : " + Thread.currentThread().getName() + " Response from server : " + verbalResponse);
					ItemServerWrapper returnedItem = null;
					if (verbalResponse != null) {
						try {
							returnedItem = new ItemActions().createObjectFromJson(verbalResponse);
						} catch (Exception e) {
							log.error("Cannot parse upload response", e);
						}

						int statusCode = returnedItem == null ? 0 : returnedItem.getMeta().getStatusCode();

						if (statusCode != 10) {
							Notification notification = buildNotification("There was a problem uploading you media, please try again later.", 100);
							nm.notify(NOTIFY_ID_UPLOAD, notification);
						} else {
							Notification notification = buildNotification("Media was uploaded successfully ! mazeltov !", 100);
							nm.notify(NOTIFY_ID_UPLOAD, notification);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								log.error("Failed to sleep", e);
							}
							nm.cancel(NOTIFY_ID_UPLOAD);
						}
					}
					stopSelf();
				} catch (ClientProtocolException e) {
					log.error(Thread.currentThread().getName() + " " + "Error on upload : ", e);
				} catch (IOException e) {
					log.error(Thread.currentThread().getName() + " " + "Error on upload : " + e.getMessage());
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.setName("Upload Service Thread #" + System.currentTimeMillis());
		thread.start();
		threadInstance = thread;
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		log.info("Upload service destroyed");
		if (threadInstance != null)
			threadInstance.interrupt();
		nm.cancel(NOTIFY_ID_UPLOAD);
		unregisterReceiver(uploadCancelReceiver);
		super.onDestroy();
	}

	private Notification buildNotification(String msg, int progress) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setWhen(System.currentTimeMillis() + 500);
		builder.setTicker(msg);
		builder.setContentTitle(getString(R.string.app_name));
		builder.setContentText(msg);
		builder.setSmallIcon(R.drawable.mazeltov_logo);
		builder.setOngoing(true);
		builder.setProgress(100, progress, false);

		return builder.build();
	}

	private BroadcastReceiver uploadCancelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (connection != null) {
				connection.clearRequestInterceptors();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		UploadService getService() {
			return UploadService.this;
		}
	}
}
