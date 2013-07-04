package net.fizzl.redditlite.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RedditListing {
	public RedditListing(String data) throws JSONException {
		items = new ArrayList<RedditListingItem>();
		parse(data);
	}
	
	public void parse(String data) throws JSONException {
		JSONArray children = 
				(new JSONObject(data))
				.getJSONObject("data")
				.getJSONArray("children");
		
		for(int i = 0;i < children.length();i++) {
			RedditListingItem newItem = new RedditListingItem(children.getJSONObject(i).toString());
			items.add(newItem);
		}
	}
	
	// Internal data
	List<RedditListingItem> items;
	public class RedditListingItem {
		public RedditListingItem(String data) throws JSONException {
			parse(data);
		}		
		
		public void parse(String data) throws JSONException {
			JSONObject o = (new JSONObject(data))
							.getJSONObject("data");
			title 		= o.getString("title");
			targetUrl 	= o.getString("url");
			commentsUrl = "http://www.reddit.com" + o.getString("permalink");
			previewUrl	= o.getString("thumbnail");
			selfPost	= o.getBoolean("is_self");
		}
		
		public String getTitle() {
			return title;
		}

		public String getTargetUrl() {
			return targetUrl;
		}

		public String getCommentsUrl() {
			return commentsUrl;
		}

		public String getPreviewUrl() {
			return previewUrl;
		}
		
		public boolean isSelfPost() {
			return selfPost;
		}
		
		private String title;
		private String targetUrl;
		private String commentsUrl;
		private String previewUrl;
		private boolean selfPost;
	}
}
