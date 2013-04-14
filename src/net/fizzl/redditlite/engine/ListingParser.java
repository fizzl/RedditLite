package net.fizzl.redditlite.engine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListingParser {
	private ListingParser() {
		
	}
	
	public ListingParser(String content) {
		this();
		this.content = content;
	}
	
	public List<ListItem> getList() throws JSONException {
		List<ListItem> ret = new ArrayList<ListItem>();
		
		JSONObject root = new JSONObject(content);
		if(!root.getString("kind").equals("Listing")) {
			throw new RuntimeException("Malforemd JSON data in parser, root.");
		}
		JSONArray items = root.getJSONObject("data").getJSONArray("children");
		for(int i = 0;i < items.length(); i++) {
			JSONObject o = items.getJSONObject(i);
			if(!o.getString("kind").equals("t3")) {
				throw new RuntimeException("Malforemd JSON data in parser, item");
			}
			o = o.getJSONObject("data");
			ListItem newItem = parseItem(o);
			ret.add(newItem);
		}
		return ret;
	}
	private ListItem parseItem(JSONObject o) throws JSONException {
		ListItem ret = new ListItem();
		ret.setScore(o.getInt("score"));
		ret.setSubreddit(o.getString("subreddit"));
		ret.setUrl(o.getString("url"));
		ret.setAuthor(o.getString("author"));
		ret.setTitle(o.getString("title"));
		ret.setOver18(o.getBoolean("over_18"));
		ret.setLinkFlairText(o.getString("link_flair_text"));
		ret.setSelf(o.getBoolean("is_self"));
		ret.setSelftext(o.getString("selftext"));
		ret.setSelftextHtml(o.getString("selftext_html"));
		
		return ret;
	}
	String content;
}
