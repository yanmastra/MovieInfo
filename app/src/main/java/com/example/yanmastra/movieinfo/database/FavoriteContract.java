package com.example.yanmastra.movieinfo.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yan Mastra on 8/22/2017.
 */

public class FavoriteContract {
    public static final String AUTHORITY = "com.example.ynmastra.movieinfo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";
    public static final class Entry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
    }
}
