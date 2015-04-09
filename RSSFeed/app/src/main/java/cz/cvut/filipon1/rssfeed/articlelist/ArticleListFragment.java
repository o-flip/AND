package cz.cvut.filipon1.rssfeed.articlelist;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.ArticleTable;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 14.3.15.
 */
public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArticleListFragmentListener listener;

    private static final int ARTICLE_LOADER = 1;
    private ArticleCursorAdapter adapter;
    private ListView listView;


    public static ArticleListFragment newInstance() {
        ArticleListFragment f = new ArticleListFragment();
        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ArticleListFragmentListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        listView = (ListView) view.findViewById(R.id.database_content);
        adapter = new ArticleCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.showArticleDetail((int)id);
            }
        });

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == ARTICLE_LOADER) {
            return new CursorLoader(getActivity(), ReaderContentProvider.CONTENT_URI_ARTICLE, null, null, null,
                    ArticleTable.UPDATED + " DESC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ARTICLE_LOADER) {
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ARTICLE_LOADER) {
            adapter.swapCursor(null);
        }
    }

    public interface ArticleListFragmentListener {
        void showArticleDetail(int index);
    }



}
