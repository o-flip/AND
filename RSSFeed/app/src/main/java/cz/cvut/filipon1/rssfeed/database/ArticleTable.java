package cz.cvut.filipon1.rssfeed.database;

import android.database.sqlite.SQLiteDatabase;
import android.text.Html;

/**
 * Created by ondra on 3.4.15.
 */
public class ArticleTable {
    public static final String TABLE_NAME = "articleTable";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String LINK = "link";
    public static final String UPDATED = "updated";
    public static final String CONTENT = "content";
    public static final String URI = "uri";
    public static final String SUMMARY = "summary";
    public static final String FEED_ID = "feedId";


    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + FEED_ID + " integer, "
            + ID + " integer primary key autoincrement, "
            + TITLE + " text not null, "
            + LINK + " text not null unique, "
            + AUTHOR + " text not null, "
            + UPDATED + " integer, "
            + CONTENT + " text, "
            + URI + " text, "
            + SUMMARY + " text "
            + ");";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        dropAndCreateTable(db);
    }

    public static void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAndCreateTable(db);
    }

    public static void dropAndCreateTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
