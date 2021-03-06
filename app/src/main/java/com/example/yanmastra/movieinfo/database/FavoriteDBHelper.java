package com.example.yanmastra.movieinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yanmastra.movieinfo.utilities.Constant;

/**
 * Created by Yan Mastra on 8/22/2017.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = Constant.DB_NAME;
    private static final int DATABASE_VERSION = 1;

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE "+ FavoriteContract.Entry.TABLE_NAME + " ("
                + FavoriteContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoriteContract.Entry.COLUMN_MOVIE_ID+" INTEGER NOT NULL, "
                + FavoriteContract.Entry.COLUMN_TITLE+" TEXT, "
                + FavoriteContract.Entry.COLUMN_POSTER+" TEXT, "
                + FavoriteContract.Entry.COLUMN_OVERVIEW+" TEXT, "
                + FavoriteContract.Entry.COLUMN_RATING+" REAL, "
                + FavoriteContract.Entry.COLUMN_RELEASE_DATE+" TEXT, "
                + FavoriteContract.Entry.COLUMN_BACKDROP+" TEXT, "
                + FavoriteContract.Entry.COLUMN_GENRE+" TEXT, "
                + FavoriteContract.Entry.COLUMN_LANGUAGE+" TEXT"
                + ");";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteContract.Entry.TABLE_NAME);
        onCreate(db);
    }
}
