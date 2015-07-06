package cz.cvut.filipon1.rssfeed.feedconfig;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 4.4.15.
 */
public class FeedListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int FEED_LOADER = 2;
    private ListView listView;
    private FeedCursorAdapter adapter;
    private static final String DELETE_DIALOG = "deleteDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list_activity);

        getLoaderManager().initLoader(FEED_LOADER, null, this);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new FeedCursorAdapter(this, null, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteFeedDialog frag = DeleteFeedDialog.newInstance(id);
                frag.show(getFragmentManager(), DELETE_DIALOG);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == FEED_LOADER) {
            return new CursorLoader(this, ReaderContentProvider.CONTENT_URI_FEED, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       switch (loader.getId()) {
           case FEED_LOADER:
               adapter.swapCursor(data);
               break;
       }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case FEED_LOADER:
                adapter.swapCursor(null);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feed_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_feed) {
            AddFeedDialog frag = AddFeedDialog.newInstance();
            frag.show(getFragmentManager(), DELETE_DIALOG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
