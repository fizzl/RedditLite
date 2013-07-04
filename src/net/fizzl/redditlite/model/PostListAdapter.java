package net.fizzl.redditlite.model;

import java.util.List;

import net.fizzl.redditlite.R;
import net.fizzl.redditlite.model.RedditListing.RedditListingItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostListAdapter extends ArrayAdapter<RedditListingItem> {

	public PostListAdapter(Context context, 
			int textViewResourceId, List<RedditListingItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(R.layout.listitem_post, parent, false);
		}
		TextView title = (TextView) row.findViewById(R.id.title);
		RedditListingItem item = items.get(position);
		title.setText(item.getTitle());
		return row;
	}
	
	public void setListing(RedditListing listing) {
		this.listing = listing;
		items.clear();
		items.addAll(listing.items);
		notifyDataSetChanged();
	}
	
	List<RedditListingItem> items;
	Context context;
	RedditListing listing;
	int textViewResource;
}
