package net.fizzl.redditlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PostListActivity extends FragmentActivity implements
		PostListFragment.Callbacks {
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_list);

		if (findViewById(R.id.post_detail_container) != null) {
			mTwoPane = true;
			((PostListFragment) getSupportFragmentManager().findFragmentById(
					R.id.post_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PostDetailFragment.ARG_ITEM_ID, id);
			PostDetailFragment fragment = new PostDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.post_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PostDetailActivity.class);
			detailIntent.putExtra(PostDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

}
