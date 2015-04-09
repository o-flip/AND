package cz.cvut.filipon1.rssfeed.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by ondra on 4.4.15.
 */
public class ReaderContentProvider extends ContentProvider {

    private ReaderDatabaseHelper readerDatabaseHelper;

    public static final String AUTHORITY = "cz.cvut.filipon1.rssfeed";

    private static final int ARTICLE_LIST = 1;
    private static final int ARTICLE_ID = 2;
    private static final int FEED_LIST = 3;
    private static final int FEED_ID = 4;


    private static final String ARTICLE_PATH = "articles";
    public static final Uri CONTENT_URI_ARTICLE = Uri.parse("content://" + AUTHORITY + "/" + ARTICLE_PATH);

    private static final String FEED_PATH = "feed";
    public static final Uri CONTENT_URI_FEED = Uri.parse("content://" + AUTHORITY + "/" + FEED_PATH);

    private static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URIMatcher.addURI(AUTHORITY, ARTICLE_PATH, ARTICLE_LIST);
        URIMatcher.addURI(AUTHORITY, ARTICLE_PATH + "/#", ARTICLE_ID);
        URIMatcher.addURI(AUTHORITY, FEED_PATH, FEED_LIST);
        URIMatcher.addURI(AUTHORITY, FEED_PATH + "/#", FEED_ID);
    }

    @Override
    public boolean onCreate() {
        readerDatabaseHelper = new ReaderDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case ARTICLE_LIST:
                queryBuilder.setTables(ArticleTable.TABLE_NAME);
                break;
            case ARTICLE_ID:
                queryBuilder.setTables(ArticleTable.TABLE_NAME);
                queryBuilder.appendWhere(ArticleTable.ID + "=" + uri.getLastPathSegment());
                break;
            case FEED_LIST:
                queryBuilder.setTables(FeedTable.TABLE_NAME);
                break;
            case FEED_ID:
                queryBuilder.setTables(FeedTable.TABLE_NAME);
                queryBuilder.appendWhere(FeedTable.ID + "=" + uri.getLastPathSegment());
                break;
        }

        SQLiteDatabase db = readerDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase db = readerDatabaseHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case ARTICLE_LIST:
                id = db.insert(ArticleTable.TABLE_NAME, null, values);
                break;
            case FEED_LIST:
                id = db.insert(FeedTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = URIMatcher.match(uri);
        SQLiteDatabase sqlDB = readerDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case FEED_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(FeedTable.TABLE_NAME, FeedTable.ID + "=" + id, null);
                break;
            case ARTICLE_LIST:
                rowsDeleted = sqlDB.delete(ArticleTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
