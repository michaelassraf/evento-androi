package com.lbis.server.upload.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.lbis.server.upload.http.FileMultipartEntity.ProgressListener;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {
	ProgressDialog pd;
	long totalSize;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Context ctx;

	public HttpMultipartPost(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(ctx);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Uploading Picture...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(String... arg0) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://www.lbisgroup.com/MazelTov/WebDev/upload.php");

		try {
			FileMultipartEntity multipartContent = new FileMultipartEntity(new ProgressListener() {
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			multipartContent.addPart("file", new FileBody(new File(arg0[0])));
			totalSize = multipartContent.getContentLength();
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			log.info("Finished upload response from server is : " + serverResponse);
			return serverResponse;
		}

		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buf = new byte[bufferSize];
		int bytesRead = input.read(buf);
		while (bytesRead != -1) {
			output.write(buf, 0, bytesRead);
			bytesRead = input.read(buf);
		}
		output.flush();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String ui) {
		pd.dismiss();
	}
}