package cz.cvut.filipon1.rssfeed.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ondra on 3.4.15.
 */
public class FeedTable {
    public static final String TABLE_NAME = "feedTable";
    public static final String ID = "_id";
    public static final String LINK = "link";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " integer primary key autoincrement, "
            + LINK + " text not null unique "
            + ");";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        ContentValues cv = new ContentValues();
        cv.put(LINK, "http://servis.idnes.cz/rss.aspx?c=technet");
        db.insert(TABLE_NAME, null, cv);
        cv.put(LINK, "http://android-developers.blogspot.com/atom.xml");
        db.insert(TABLE_NAME, null, cv);
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
