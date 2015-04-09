package cz.cvut.filipon1.rssfeed.articlelist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.ArticleTable;

/**
 * Created by ondra on 4.4.15.
 */
public class ArticleCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private Context context;



    public ArticleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_article_list, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.item_article_title))
                .setText(cursor.getString(cursor.getColumnIndex(ArticleTable.TITLE)));

        ((TextView) view.findViewById(R.id.item_article_subtitle))
                .setText(cursor.getString(cursor.getColumnIndex(ArticleTable.SUMMARY)));
    }
}
