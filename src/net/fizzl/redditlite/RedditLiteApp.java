package net.fizzl.redditlite;

import android.app.Application;
import android.content.Context;

public class RedditLiteApp extends Application {
	public static Context getAppContext() {
		return me.getApplicationContext();
	}
	public RedditLiteApp() {
		me = this;
	}
	private static RedditLiteApp me;
}
