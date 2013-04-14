package net.fizzl.redditlite.ui;

import java.net.URI;
import java.net.URISyntaxException;

import net.fizzl.redditlite.R;
import net.fizzl.redditlite.engine.ListItem;
import net.fizzl.redditlite.engine.ListItemAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
			initListViews();
		} catch (URISyntaxException e) {
			Toast.makeText(this, R.string.could_not_initialize_ui, Toast.LENGTH_LONG)
			.show();
		}
        
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

	private void initListViews() throws URISyntaxException {
        list =  getListView();
        list.setAdapter(new ListItemAdapter(this, R.layout.list_item, new URI(getResources().getString(R.string.uriHot))));
        list.setOnItemClickListener(this);
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.action_refresh:
    		refresh();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
	@Override
	public void onItemClick(AdapterView<?> adapterview, View v, int position, long id) {
    	ListItemAdapter adapter= (ListItemAdapter) getListAdapter();
    	ListItem item = adapter.getItem(position);
    	String url = item.getUrl();
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(url));
    	startActivity(intent);   		
	}
	
    private void refresh() {
    	ListItemAdapter adapter= (ListItemAdapter) list.getAdapter();
    	adapter.refresh();
	}

    ListView list;
}
