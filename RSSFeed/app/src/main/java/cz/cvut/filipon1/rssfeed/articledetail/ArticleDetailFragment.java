package cz.cvut.filipon1.rssfeed.articledetail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.ArticleTable;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 14.3.15.
 */
public class ArticleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String ARTICLE_INDEX = "articleIndex";
    private static final int ARTICLE_LOADER = 1;

    TextView tvTitle;
    TextView tvContent;
    TextView tvURL;
    TextView tvAuthor;
    TextView tvUpdated;

    Cursor cursor;

    String URL;

    public static ArticleDetailFragment newInstance(int index) {
        ArticleDetailFragment f = new ArticleDetailFragment();

        Bundle args = new Bundle();
        args.putInt(ARTICLE_INDEX, index);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);

        tvTitle = (TextView)view.findViewById(R.id.tvDetailTitle);
        tvContent = (TextView)view.findViewById(R.id.tvDetailContent);
        tvURL = (TextView)view.findViewById(R.id.tvURL);
        tvAuthor = (TextView)view.findViewById(R.id.tvAuthor);
        tvUpdated= (TextView)view.findViewById(R.id.tvUpdated);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareArticle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareArticle() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, URL);
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int idArticle = getArguments().getInt(ARTICLE_INDEX);
        return new CursorLoader(getActivity()
                , Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI_ARTICLE, Integer.toString(idArticle))
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        cursor.moveToFirst();

        tvURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });

        URL = cursor.getString(cursor.getColumnIndex(ArticleTable.LINK));

        tvUpdated.setText(DateUtils.getRelativeTimeSpanString(cursor.getLong(cursor.getColumnIndex(ArticleTable.UPDATED))));
        tvTitle.setText(cursor.getString(cursor.getColumnIndex(ArticleTable.TITLE)));
        if (!cursor.getString(cursor.getColumnIndex(ArticleTable.AUTHOR)).isEmpty())
            tvAuthor.setText(cursor.getString(cursor.getColumnIndex(ArticleTable.AUTHOR)));
        String content = cursor.getString(cursor.getColumnIndex(ArticleTable.CONTENT));
        content = content.replaceAll("(?s)<style[^>]*>.*?</style>", "");
        content = content.replaceAll("(?s)<img.*?/>", "");
        content = content.replaceAll("(?s)<a[^>]*>\\s*</a>", "");
        content = content.replaceAll("(?s)<ul[^>]*>(.*?)</ul>", "<p>$1</p>");
        content = content.replaceAll("(?s)<li[^>]*>(.*?)</li>", "<br/>&bull; $1");
        content = content.replaceAll("(?s)(<br.*?/>\\s*)+", "<br/>");
        tvContent.setText(Html.fromHtml(content));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
