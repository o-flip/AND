package cz.cvut.filipon1.rssfeed.feedconfig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.os.Bundle;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.ArticleTable;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 4.4.15.
 */
public class DeleteFeedDialog extends DialogFragment {
    private static final String FEED_ID = "feedId";

    private long feedId;

    public static DeleteFeedDialog newInstance(long id) {
        DeleteFeedDialog f = new DeleteFeedDialog();

        Bundle b = new Bundle();
        b.putLong(FEED_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        feedId = getArguments().getLong(FEED_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_dialog_title));

        builder.setPositiveButton(getString(R.string.dialog_delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentResolver resolver = getActivity().getContentResolver();
                resolver.delete(ReaderContentProvider.CONTENT_URI_ARTICLE, ArticleTable.FEED_ID + "=?",
                        new String[] {String.valueOf(feedId)});
                resolver.delete(ContentUris.withAppendedId(ReaderContentProvider.CONTENT_URI_FEED, feedId),
                        null, null);
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel_button), null);

        return builder.create();
    }
}
