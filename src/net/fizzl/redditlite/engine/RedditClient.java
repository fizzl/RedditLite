package net.fizzl.redditlite.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class RedditClient extends AsyncTask<URI, Integer, String> {
	
	// From AsyncTask
	@Override
	protected String doInBackground(URI... urls) {
		HttpResponse resp;
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		
		if(urls.length != 1) {
			throw new RuntimeException("RedditClient takes exactly one url to run.");
		}
		HttpGet get = new HttpGet(urls[0]);
		try {
			resp = httpClient.execute(get);
		} catch (IOException e) {
			return "";
		}
		HttpEntity entity = resp.getEntity();
		try {
			entity.writeTo(os);
		} catch (IOException e) {
			return "";
		}
		
		try {
			return new String(os.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.refreshDone(result);
	}

	// Internal methods
	private RedditClient() {

	}

	public RedditClient(Context ctx, RedditClientCallback callback) {
		this();
		this.context = ctx;
		this.callback = callback;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize RedditClient");
		}

		userAgent = String.format("RedditLite %s %s (%d)", info.packageName,
				info.versionName, info.versionCode);
		httpClient = AndroidHttpClient.newInstance(userAgent);
	}

	public void release() {
		if (httpClient != null) {
			httpClient.close();
			httpClient = null;
		}
	}

	String ret = null;
	AndroidHttpClient httpClient;
	Context context;
	String userAgent;
	RedditClientCallback callback;
}
