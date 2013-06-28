package net.fizzl.redditlite.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class SynchronousHttpClient {
	public SynchronousHttpClient(Context context) {
		this.context = context;
		client.setCookieStore(cookieStore);
		String packageName = context.getPackageName();
		String version;
		try {
			version = context.getPackageManager()
					.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			version = "Unknown Version";
			e.printStackTrace();
		}

		String agent = packageName + "/RedditLite/" + version;
		HttpProtocolParams.setUserAgent(client.getParams(), agent);
	}

	public byte[] get(String baseUrl, List<NameValuePair> params)
			throws SynchronousHttpClientException {
		if (params != null) {
			String paramsString = URLEncodedUtils.format(params, "UTF-8");
			baseUrl += paramsString;
		}
		try {
			URI uri = new URI(baseUrl);
			HttpGet get = new HttpGet(uri);
			HttpResponse response;
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new SynchronousHttpClientException(
						"Failed to get entity for connection");
			}
			InputStream is = entity.getContent();
			return streamToByteArray(is);
		} catch (Exception e) {
			throw new SynchronousHttpClientException(e);
		}
	}

	public byte[] post(String baseUrl, List<NameValuePair> params)
			throws SynchronousHttpClientException {
		try {
			URI uri = new URI(baseUrl);
			HttpPost post = new HttpPost(uri);
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new SynchronousHttpClientException(
						"Failed to get entity for connection");
			}
			InputStream is = entity.getContent();
			return streamToByteArray(is);
		} catch (Exception e) {
			throw new SynchronousHttpClientException(e);
		}
	}

	private byte[] streamToByteArray(InputStream is) throws IOException {
		return IOUtils.toByteArray(is);
	}

	public CookieStore getCookieStrore() {
		return cookieStore;
	}
	
	// Locals
	Context context;
	static DefaultHttpClient client = new DefaultHttpClient();
	static CookieStore cookieStore = new BasicCookieStore();
}
