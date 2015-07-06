package cz.cvut.filipon1.rssfeed.articlelist;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import cz.cvut.filipon1.rssfeed.articledetail.ArticleDetailActivity;
import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.feedconfig.FeedListActivity;


public class ArticleListActivity extends ActionBarActivity
        implements ArticleListFragment.ArticleListFragmentListener, DownloaderFragment.TaskCallbacks {

    public static final String ARTICLE_INDEX = "articleIndex";
    private static final String DOWNLOAD_FRAGMENT = "downloadFragment";
    private DownloaderFragment downloaderFragment;
    MenuItem refresh;
    View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        progressBar = LayoutInflater.from(this).inflate(R.layout.progress, null);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.contariner, ArticleListFragment.newInstance())
                    .commit();
            downloaderFragment = new DownloaderFragment();
            getFragmentManager().beginTransaction()
                    .add(downloaderFragment, DOWNLOAD_FRAGMENT)
                    .commit();
        }

    }

    @Override
    public void showArticleDetail(int index) {
        Intent i = new Intent(this, ArticleDetailActivity.class);
        Bundle b = new Bundle();
        b.putInt(ARTICLE_INDEX, index);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article_list, menu);
        refresh = menu.findItem(R.id.action_refresh);
        DownloaderFragment df = (DownloaderFragment) getFragmentManager().findFragmentByTag(DOWNLOAD_FRAGMENT);
        if ( df != null && df.isRunning()) {
            refresh.setActionView(progressBar);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshArticles();
            return true;
        }
        if (item.getItemId() == R.id.feed_config) {
            Intent i = new Intent(this, FeedListActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshArticles() {
        ((DownloaderFragment)getFragmentManager().findFragmentByTag(DOWNLOAD_FRAGMENT)).executeTask();
    }

    @Override
    public void onPostExecute(ContentValues s) {
        refresh.setActionView(null);
    }

    @Override
    public void onPreExecute() {
        refresh.setActionView(progressBar);
    }
}
