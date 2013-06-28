package net.fizzl.redditlite.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Context;

public class RedditAuthStore {
	private static final String FILE_NAME = "authStore.obj";
	public RedditAuthStore(Context context) {
		this.context = context;
		loadStore();
	}
	
	public void saveStore(String username, String password, Cookie cookie, String modhash) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
			oos.writeObject(username);
			oos.writeObject(password);
			oos.writeObject(cookie.getValue());
			oos.writeObject(cookie.getExpiryDate());
			oos.writeObject(modhash);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void loadStore() {
		try {
			ObjectInputStream ois = new ObjectInputStream(context.openFileInput(FILE_NAME));
			username = (String) ois.readObject();
			password = (String) ois.readObject();
			String cookie = (String) ois.readObject();
			Date expr = (Date) ois.readObject();
			BasicClientCookie tmpCookie = new BasicClientCookie("reddit_session", cookie);
			tmpCookie.setExpiryDate(expr);
			authCookie = tmpCookie;
			modhash = (String) ois.readObject();
			ois.close();
		} catch (Exception e) {
			username = null;
			password = null;
			authCookie = null;
			modhash = null;
		}
	}
	
	// Stored credentials
	private String username, password, modhash;
	private Cookie authCookie;
	
	// Accessors
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getModhash() {
		return modhash;
	}
	public Cookie getAuthCookie() {
		return authCookie;
	}
	
	// Runtime
	private Context context;
}
