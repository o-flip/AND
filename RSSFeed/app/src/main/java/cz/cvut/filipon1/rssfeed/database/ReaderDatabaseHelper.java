package cz.cvut.filipon1.rssfeed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ondra on 3.4.15.
 */
public class ReaderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Feed25.db";
    private static final int DB_VERSION = 1;

    public ReaderDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArticleTable.onCreate(db);
        FeedTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArticleTable.onUpgrade(db, oldVersion, newVersion);
        FeedTable.onUpgrade(db, oldVersion, newVersion);
    }
}
