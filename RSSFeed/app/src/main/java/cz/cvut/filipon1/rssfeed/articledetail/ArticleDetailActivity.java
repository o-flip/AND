package cz.cvut.filipon1.rssfeed.articledetail;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.articlelist.ArticleListActivity;

/**
 * Created by ondra on 14.3.15.
 */
public class ArticleDetailActivity extends ActionBarActivity {

    public static final String DETAIL_FRAGMENT = "detailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState == null) {
            int index = getIntent().getIntExtra(ArticleListActivity.ARTICLE_INDEX, 1);
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, ArticleDetailFragment.newInstance(index), DETAIL_FRAGMENT)
                    .commit();
        }
    }




}
