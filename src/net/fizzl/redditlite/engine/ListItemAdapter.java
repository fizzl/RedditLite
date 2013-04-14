package net.fizzl.redditlite.engine;

import java.net.URI;
import java.util.List;

import net.fizzl.redditlite.R;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListItemAdapter extends ArrayAdapter<ListItem> implements
		RedditClientCallback {
	// Own methods
	public ListItemAdapter(Context context, int textViewResourceId, URI uri) {
		super(context, textViewResourceId);
		this.uri = uri;
		this.context = context;
	}

	public void refresh() {
		URI params[] = new URI[1];
		params[0] = uri;
		RedditClient client = new RedditClient(context, this);
		client.execute(params);
	}

	synchronized private void updateList(String json) throws JSONException {
		ListingParser p = new ListingParser(json);
		clear();
		List<ListItem> newItems = p.getList();
		for (ListItem i : newItems) {
			add(i);
		}
	}

	// From ArrayAdapter
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		if (v == null) {
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.list_item, null);
		}
		ListItem item = getItem(position);
		if (item == null) {
			return null;
		}
		TextView title = (TextView) v.findViewById(R.id.txtTitle);
		TextView score = (TextView) v.findViewById(R.id.txtScore);
		TextView user = (TextView) v.findViewById(R.id.txtUser);
		TextView subreddit = (TextView) v.findViewById(R.id.txtSubreddit);

		title.setText(item.getTitle());
		score.setText(Integer.toString(item.getScore()));
		user.setText(item.getAuthor());
		subreddit.setText(item.getSubreddit());

		return v;
	}

	// From RedditClientCallback
	@Override
	public void refreshDone(String result) {
		try {
			updateList(result);
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(context, R.string.problem_parsing_data,
					Toast.LENGTH_LONG).show();
		}
	}

	Context context;
	URI uri;
}
