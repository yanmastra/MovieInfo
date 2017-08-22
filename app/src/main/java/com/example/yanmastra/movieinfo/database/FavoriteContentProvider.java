package com.example.yanmastra.movieinfo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Yan Mastra on 8/22/2017.
 */

public class FavoriteContentProvider extends ContentProvider {
    private FavpriteDBHelper dbHelper;
    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        dbHelper = new FavpriteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result = null;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match){
            case FAVORITES :
                long id = db.insert(FavoriteContract.Entry.TABLE_NAME, null, values);
                if (id > 0){
                    result = ContentUris.withAppendedId(FavoriteContract.Entry.CONTENT_URI, id);
                }else {
                    throw new SQLException("Insert data failed to "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+uri);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);
        return matcher;
    }
}
