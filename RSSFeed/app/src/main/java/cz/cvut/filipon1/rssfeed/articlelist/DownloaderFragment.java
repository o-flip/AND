package cz.cvut.filipon1.rssfeed.articlelist;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;


import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import cz.cvut.filipon1.rssfeed.database.ArticleTable;
import cz.cvut.filipon1.rssfeed.database.FeedTable;
import cz.cvut.filipon1.rssfeed.database.ReaderContentProvider;

/**
 * Created by ondra on 3.4.15.
 */
public class DownloaderFragment extends Fragment {

    private TaskCallbacks callbacks;
    private DownloadTask task;

    public static interface TaskCallbacks {
        public void onPostExecute(ContentValues s);
        public void onPreExecute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (TaskCallbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void executeTask() {
        task = new DownloadTask();
        task.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public boolean isRunning() {
        if (task == null)
            return false;
        return task.getStatus() == AsyncTask.Status.RUNNING;
    }

    public class DownloadTask extends AsyncTask<Void, Void, ContentValues> {

        @Override
        protected ContentValues doInBackground(Void... params) {

            ContentValues cv = new ContentValues();
            XmlReader reader;

            Cursor cursor = getActivity().getContentResolver()
                    .query(ReaderContentProvider.CONTENT_URI_FEED, null, null, null, null, null);
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String link = cursor.getString(cursor.getColumnIndex(FeedTable.LINK));
                int feedId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedTable.ID)));
                cursor.moveToNext();
                try {
                    reader = new XmlReader(new URL(link));
                    SyndFeed feed = new SyndFeedInput().build(reader);

                    for (Iterator i = feed.getEntries().iterator(); i.hasNext(); ) {
                        SyndEntry entry = (SyndEntry) i.next();
                        cv.put(ArticleTable.FEED_ID, feedId);
                        cv.put(ArticleTable.TITLE, entry.getTitle());
                        cv.put(ArticleTable.AUTHOR, entry.getAuthor());
                        cv.put(ArticleTable.LINK, entry.getLink());
                        cv.put(ArticleTable.URI, entry.getUri());
                        cv.put(ArticleTable.UPDATED, entry.getPublishedDate().getTime());

                        String content;
                        if (entry.getContents().size() > 0) {
                            content = ((SyndContent) entry.getContents().get(0)).getValue();
                        } else if (entry.getDescription() != null) {
                            content = entry.getDescription().getValue();
                        } else {
                            content = "";
                        }
                        cv.put(ArticleTable.CONTENT, content);

                        String sum = "";
                        if (entry.getDescription() != null) {
                            sum = entry.getDescription().getValue();
                        } else if (entry.getContents().size() > 0) {
                            sum = ((SyndContent) entry.getContents().get(0)).getValue();
                        }
                        cv.put(ArticleTable.SUMMARY, summaryTrim(sum));

                        insertContentValue(cv);
                    }
                } catch (FeedException ex) {
                    ex.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            return cv;
        }

        @Override
        protected void onPreExecute() {
            callbacks.onPreExecute();
        }

        private void insertContentValue(ContentValues cv) {
            if (getActivity() != null)
                getActivity().getContentResolver().insert(ReaderContentProvider.CONTENT_URI_ARTICLE, cv);
        }

        @Override
        protected void onPostExecute(ContentValues s) {
            if (callbacks != null) {
                callbacks.onPostExecute(s);
            }
        }

        private String summaryTrim(String text) {
            text = text.replaceAll("(?s)<style[^>]*>.*?</style>", "");
            text = text.replaceAll("(?s)<img.*?/>", "");
            text = Html.fromHtml(text).toString();
            if (text.length() > 150) {
                text = text.substring(0, 150);
            }
            return text.trim() + "...";
        }
    }
}

