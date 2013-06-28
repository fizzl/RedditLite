package net.fizzl.redditlite.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.fizzl.redditlite.R;
import net.fizzl.redditlite.RedditLiteApp;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RedditAuthentication {
	public void login(String username, String password, boolean stayLoggedIn,
			RedditLoginCallback callback) {
		LoginTaskParam param = new LoginTaskParam();
		param.username = username;
		param.password = password;
		param.stayLoggedIn = stayLoggedIn;
		param.callback = callback;
		LoginTask task = new LoginTask();
		task.execute(param);
	}

	public static Cookie getAuthCookie() {
		return authCookie;
	}

	private static Cookie authCookie;

	// Callback
	public interface RedditLoginCallback {
		public void loginSuccess(Cookie authCookie, String modhash);
		public void loginFailed(Exception e, int message);
	}

	// Info container for task
	public class LoginTaskParam {
		public String username;
		public String password;
		public boolean stayLoggedIn;
		public RedditLoginCallback callback;
	}

	// Return type of asynctask
	public class LoginTaskReturn {
		public boolean success;
		public Cookie cookie;
		public String modhash;
		public Exception e;
		public int errorMessage;
	}

	// Async task to do the login
	private class LoginTask extends
			AsyncTask<LoginTaskParam, Integer, LoginTaskReturn> {

		@Override
		protected LoginTaskReturn doInBackground(LoginTaskParam... params) {
			param = params[0];
			SynchronousHttpClient client = new SynchronousHttpClient(
					RedditLiteApp.getAppContext());
			List<NameValuePair> postParams = new ArrayList<NameValuePair>(4);
			postParams.add(new BasicNameValuePair("api_type", "json"));
			postParams.add(new BasicNameValuePair("user", param.username));
			postParams.add(new BasicNameValuePair("passwd", param.password));
			postParams.add(new BasicNameValuePair("rem", Boolean
					.toString(param.stayLoggedIn)));
			LoginTaskReturn ret = new LoginTaskReturn();
			byte result[] = null;
			String strResult = null;
			try {
				result = client.post("http://www.reddit.com/api/login",
						postParams);
			} catch (SynchronousHttpClientException e) {
				ret.success = false;
				ret.e = e;
				ret.errorMessage = R.string.could_not_connect;
				return ret;
			}
			try {
				strResult = new String(result, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				ret.success = false;
				ret.e = e;
				ret.errorMessage = R.string.weird_data;
				return ret;
			}
			CookieStore cookies = client.getCookieStrore();
			for(Cookie k : cookies.getCookies()) {
				if(k.getName().equals("reddit_session")) {
					authCookie = k;
					break;
				}
			}
			if(authCookie == null) {
				ret.e = null;
				ret.errorMessage = R.string.login_failed;
			}
			else {
				try {
					ret.modhash = new JSONObject(strResult)
										.getJSONObject("json")
										.getJSONObject("data")
										.getString("modhash");
					
					ret.success = true;
					ret.cookie = authCookie;
				} catch (JSONException e) {
					ret.success = false;
					ret.e = e;
					ret.errorMessage = R.string.weird_data;
					return ret;
				}
			}
			
			
			return ret;
		}

		protected void onPostExecute(LoginTaskReturn result) {
			if (result.success) {
				param.callback.loginSuccess(result.cookie, result.modhash);
			} else {
				param.callback.loginFailed(result.e, result.errorMessage);
			}
		}

		LoginTaskParam param;
	}
}
