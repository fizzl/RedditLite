package net.fizzl.redditlite;

import java.io.UnsupportedEncodingException;

import net.fizzl.redditlite.model.PostListAdapter;
import net.fizzl.redditlite.model.RedditListing;
import net.fizzl.redditlite.model.SynchronousHttpClient;
import net.fizzl.redditlite.model.SynchronousHttpClientException;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PostListActivity extends FragmentActivity implements
		PostListFragment.Callbacks {
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_list);
		list = ((PostListFragment) getSupportFragmentManager().findFragmentById(
				R.id.post_list));

		if (findViewById(R.id.post_detail_container) != null) {
			mTwoPane = true;
			list.setActivateOnItemClick(true);
		}
		refresh();
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(PostDetailFragment.ARG_ITEM_ID, id);
			PostDetailFragment fragment = new PostDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.post_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, PostDetailActivity.class);
			detailIntent.putExtra(PostDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	private void refresh() {
		PostListTask task = new PostListTask();
		task.execute("http://www.reddit.com/hot.json");

	}

	private void refreshSuccess(RedditListing listing) {
		PostListAdapter adapter = (PostListAdapter) list.getListAdapter();
		adapter.setListing(listing);
	}

	private void refreshFail(int errorCode) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle(R.string.error)
		.setMessage(errorCode);
		alertBuilder.setNeutralButton(R.string.ok, null);
		AlertDialog alert = alertBuilder.create();
		alert.show();
	}

	private class PostListTaskReturn {
		RedditListing result;
		Exception e;
		int errorMessage;
	}

	private class PostListTask extends
			AsyncTask<String, Integer, PostListTaskReturn> {

		@Override
		protected PostListTaskReturn doInBackground(String... params) {
			SynchronousHttpClient client = new SynchronousHttpClient(
					PostListActivity.this);
			String url = params[0];
			PostListTaskReturn ret = new PostListTaskReturn();
			try {
				String data = new String(client.get(url, null), "UTF-8");
				ret.result = new RedditListing(data);
			} catch (UnsupportedEncodingException e) {
				ret.e = e;
				ret.errorMessage = R.string.weird_data;
			} catch (SynchronousHttpClientException e) {
				ret.e = e;
				ret.errorMessage = R.string.could_not_connect;
			} catch (JSONException e) {
				ret.e = e;
				ret.errorMessage = R.string.weird_data;
			}
			return ret;
		}

		protected void onPostExecute(PostListTaskReturn result) {
			if (result.result != null) {
				refreshSuccess(result.result);
			} else {
				refreshFail(result.errorMessage);
				result.e.printStackTrace();
			}
		}
	}
	
	PostListFragment list;
}
