package cz.cvut.filipon1.rssfeed.feedconfig;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.FeedTable;

/**
 * Created by ondra on 4.4.15.
 */
public class FeedCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public FeedCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_feed_list, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.feedURL)).setText(cursor.getString(cursor.getColumnIndex(FeedTable.LINK)));
    }
}
