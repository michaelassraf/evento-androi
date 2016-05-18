package com.lbis.server;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;

import android.content.Context;
import android.content.Intent;
import android.util.Xml.Encoding;

import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.server.objects.ServerMeta;
import com.lbis.server.objects.ServerMultipleWrapper;
import com.lbis.server.objects.ServerSingleWrapper;
import com.lbis.server.requests.SearchRequest;
import com.lbis.server.requests.SinceRequest;
import com.lbis.server.upload.http.UploadService;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.URLs;
import com.lbis.utils.GsonSerializer;

public abstract class WebRequester<CLASSTYPE, SINGLEWRAPPER extends ServerSingleWrapper<CLASSTYPE>, MULTIPLEWRAPPER extends ServerMultipleWrapper<CLASSTYPE>> {

	final Logger log = Logger.getLogger(getClass().getSimpleName());

	private static String userAgent = null;

	public static String getUserAgent() {
		if (userAgent == null)
			userAgent = System.getProperty("http.agent");
		return userAgent;
	}

	public StringEntity makeJsonRequest(CLASSTYPE object, Context context) throws UnsupportedEncodingException {
		ServerSingleWrapper<CLASSTYPE> request = new ServerSingleWrapper<CLASSTYPE>(getUpdatedServerMeta(context), object);
		StringEntity requestEntity = new StringEntity(GsonSerializer.getInstance().toJson(request).toString(), Encoding.UTF_8.toString());
		requestEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		log.info("Json " + GsonSerializer.getInstance().toJson(request).toString());
		return requestEntity;
	}

	public String makeStringJsonRequest(CLASSTYPE object, Context context) throws UnsupportedEncodingException {
		ServerSingleWrapper<CLASSTYPE> request = new ServerSingleWrapper<CLASSTYPE>(getUpdatedServerMeta(context), object);
		return GsonSerializer.getInstance().toJson(request).toString();
	}

	public StringEntity makeSinceJsonRequest(Context context, SinceRequest object) throws UnsupportedEncodingException {
		ServerSingleWrapper<SinceRequest> request = new ServerSingleWrapper<SinceRequest>(getUpdatedServerMeta(context), object);
		StringEntity requestEntity = new StringEntity(GsonSerializer.getInstance().toJson(request).toString(), Encoding.UTF_8.toString());
		requestEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		log.info("Json " + GsonSerializer.getInstance().toJson(request).toString());
		return requestEntity;
	}

	public StringEntity makeSearchJsonRequest(Context context, SearchRequest object) throws UnsupportedEncodingException {
		ServerSingleWrapper<SearchRequest> request = new ServerSingleWrapper<SearchRequest>(getUpdatedServerMeta(context), object);
		StringEntity requestEntity = new StringEntity(GsonSerializer.getInstance().toJson(request).toString(), Encoding.UTF_8.toString());
		requestEntity.setContentType("application/json");
		log.info("Json " + GsonSerializer.getInstance().toJson(request).toString());
		return requestEntity;
	}

	public LinkedList<CLASSTYPE> getAllObjectsSince(long since, DefaultHttpClient connection, Context context, String prefix) throws UnsupportedEncodingException, Exception {
		MULTIPLEWRAPPER serialized = createObjectsFromJson(invokeHTTPRequest(getAllObjectsSinceUrl(), makeSinceJsonRequest(context, new SinceRequest(getClassType().getSimpleName() + prefix, since)), connection));
		storeUpdatedServerMeta(context, serialized.getMeta());
		return serialized.getData();
	}

	public LinkedList<CLASSTYPE> getObjectsForObject(CLASSTYPE object, DefaultHttpClient connection, Context context, String prefix) throws UnsupportedEncodingException, Exception {
		MULTIPLEWRAPPER serialized = createObjectsFromJson(invokeHTTPRequest(getObjectsForObjectUrl(), makeJsonRequest(object, context), connection));
		storeUpdatedServerMeta(context, serialized.getMeta());
		return serialized.getData();
	}

	public LinkedList<CLASSTYPE> getObjectsForObject(CLASSTYPE object, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return getObjectsForObject(object, connection, context, "");
	}

	public LinkedList<CLASSTYPE> getAllObjectsSince(long since, DefaultHttpClient connection, Context context) throws Exception {
		return this.getAllObjectsSince(since, connection, context, getPullSincePrefix());
	}

	public LinkedList<CLASSTYPE> getAllObjectsInit(DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return this.getAllObjectsSince(0, connection, context, getInitializeSincePrefix());
	}

	public LinkedList<CLASSTYPE> searchForObjects(String searchPhrase, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		MULTIPLEWRAPPER serialized = createObjectsFromJson(invokeHTTPRequest(getSearchObjectsUrl(), makeSearchJsonRequest(context, new SearchRequest(searchPhrase, getClassType().getSimpleName())), connection));
		storeUpdatedServerMeta(context, serialized.getMeta());
		return serialized.getData();
	}

	public SINGLEWRAPPER createObjectFromJson(String json) {
		log.info("Json " + json);
		return GsonSerializer.getInstance().fromJson(json, getSingleClassType());
	}

	public MULTIPLEWRAPPER createObjectsFromJson(String json) {
		log.info("Json " + json);
		return GsonSerializer.getInstance().fromJson(json, getMultipleClassType());
	}

	public String invokeHTTPRequest(Enums.URLs url, StringEntity requestEntity, DefaultHttpClient connection) {
		String verbalResponse = null;
		int numTries = 5;
		int currentTries = 0;
		while (true) {
			try {
				connection.getParams().setParameter(CoreProtocolPNames.USER_AGENT, getUserAgent());
				HttpPost postMethod = new HttpPost(url.getValue());
				postMethod.setEntity(requestEntity);
				HttpResponse response = connection.execute(postMethod);
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && numTries > 0) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					verbalResponse = out.toString();
					verbalResponse = "Server is down";
					throw new Exception("Http reposnse is empty");
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				verbalResponse = out.toString();
				log.info("HTTP " + verbalResponse);
				log.info("URL is " + url.getValue());
				if (verbalResponse == null && numTries > 0)
					throw new Exception("Http reposnse is empty");
				break;
			} catch (Exception e) {
				log.info("Couldn't get response from " + url.getValue() + " trying for the " + currentTries + " time");
				if (++currentTries == numTries) {
					verbalResponse = e.getMessage();
					log.error("Wow I tried lot of times (" + currentTries + ") HTTP request failed due - ", e);
					break;
				}
			}
		}
		return verbalResponse;
	}

	public CLASSTYPE createNewRequest(CLASSTYPE object, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return getDeserializedObject(object, connection, context, createNewRequestUrl());
	}

	public CLASSTYPE getObjectForObject(CLASSTYPE object, DefaultHttpClient connection, Context context) throws UnsupportedEncodingException, Exception {
		return getDeserializedObject(object, connection, context, getObjectForObjectUrl());
	}

	public CLASSTYPE getObjectForObject(CLASSTYPE object, DefaultHttpClient connection, Context context, URLs url) throws UnsupportedEncodingException, Exception {
		return getDeserializedObject(object, connection, context, url);
	}

	private CLASSTYPE getDeserializedObject(CLASSTYPE object, DefaultHttpClient connection, Context context, Enums.URLs url) throws UnsupportedEncodingException, Exception {
		SINGLEWRAPPER serialized = createObjectFromJson(invokeHTTPRequest(url, makeJsonRequest(object, context), connection));
		storeUpdatedServerMeta(context, serialized.getMeta());
		return serialized.getData();
	}

	public ServerMeta getUpdatedServerMeta(Context context) {
		return new ServerMeta(new ExecuteManagementMethods().getTokenAndUserId(context));
	}

	public void storeUpdatedServerMeta(final Context context, final ServerMeta meta) {
		if (meta != null && meta.getToken() != null)
			new Thread() {
				public void run() {
					new ExecuteManagementMethods().setTokenAndUserId(context, meta);
				}
			}.start();
	}

	public void putFile(Context ctx, Long connetctedId, ContentTypes contentType, String filePath) {
		Intent intent = new Intent(ctx, UploadService.class);
		intent.addFlags(Context.BIND_AUTO_CREATE);
		intent.putExtra(UploadService.ARG_CONNECTED_ID, connetctedId);
		intent.putExtra(UploadService.ARG_CONTENT_TYPE, contentType.ordinal());
		intent.putExtra(UploadService.ARG_FILE_PATH, filePath);
		ctx.startService(intent);
	}

	public abstract Class<CLASSTYPE> getClassType();

	public abstract String getClassName();

	public abstract Enums.URLs createNewRequestUrl();

	public abstract Enums.URLs getObjectForObjectUrl();

	public abstract Enums.URLs getObjectsForObjectUrl();

	public abstract Enums.URLs getAllObjectsSinceUrl();

	public abstract Enums.URLs getSearchObjectsUrl();

	public abstract Class<SINGLEWRAPPER> getSingleClassType();

	public abstract Class<MULTIPLEWRAPPER> getMultipleClassType();

	public abstract String getInitializeSincePrefix();

	public abstract String getPullSincePrefix();

}