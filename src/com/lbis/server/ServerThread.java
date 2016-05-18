package com.lbis.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lbis.utils.Enums;
import com.lbis.utils.Enums.URLs;

public class ServerThread implements Runnable {
	private volatile String verbalResponse;
	Enums.URLs url;
	StringEntity requestEntity;
	DefaultHttpClient connection;

	public ServerThread(URLs url, StringEntity requestEntity, DefaultHttpClient connection) {
		this.url = url;
		this.requestEntity = requestEntity;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			HttpPost postMethod = new HttpPost(url.getValue());
			postMethod.setEntity(requestEntity);
			HttpResponse response = connection.execute(postMethod);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				verbalResponse = "Server is down";

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			verbalResponse = out.toString();
		} catch (IOException ex) {
			verbalResponse = ex.getMessage();
		}
	}

	public String getVerbalResponse() {
		return verbalResponse;
	}
}