package cz.cvut.filipon1.rssfeed.feedconfig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import cz.cvut.filipon1.rssfeed.R;
import cz.cvut.filipon1.rssfeed.database.FeedTable;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 4.4.15.
 */
public class AddFeedDialog extends DialogFragment {

    public static AddFeedDialog newInstance() {
        AddFeedDialog f = new AddFeedDialog();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.add_feed_dialog, null);
        final EditText etURL = (EditText) view.findViewById(R.id.etURL);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.add_dialog_title));
        builder.setView(view);

        builder.setPositiveButton(getString(R.string.dialog_add_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = etURL.getText().toString();
                if (!url.isEmpty()) {
                    ContentValues cv = new ContentValues();
                    cv.put(FeedTable.LINK, url);
                    getActivity().getContentResolver().insert(ReaderContentProvider.CONTENT_URI_FEED, cv);
                }
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel_button), null);

        return builder.create();
    }
}
