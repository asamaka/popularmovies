package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Calendar;

/**
 * Created by asser on 8/19/16.
 */

public class FavoriteMoviesContract
{
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "favorite_movies";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final int COLUMN_MOVIE_DB_ID_INDEX = 1;
        public static final int COLUMN_TITLE_INDEX = 2;
        public static final int COLUMN_POSTER_URL_INDEX = 3;
        public static final int COLUMN_OVERVIEW_INDEX = 4;
        public static final int COLUMN_VOTE_AVERAGE_INDEX = 5;
        public static final int COLUMN_VOTE_COUNT_INDEX = 6;
        public static final int COLUMN_POPULARITY_INDEX = 7;
        public static final int COLUMN_RELEASE_YEAR_INDEX = 8;

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_DB_ID =  "movie_db_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RELEASE_YEAR = "release_year";

        public static Uri buildFavoriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
