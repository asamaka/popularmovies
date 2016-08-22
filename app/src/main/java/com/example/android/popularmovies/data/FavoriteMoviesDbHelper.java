package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAV_MOVIES_TABLE = "CREATE TABLE " + FavoriteMoviesContract.MovieEntry.TABLE_NAME + " (" +
                FavoriteMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID + " LONG UNIQUE NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " FLOAT NOT NULL ," +
                FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT + " LONG NOT NULL ," +
                FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY + " FLOAT NOT NULL ," +
                FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_YEAR + " INT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAV_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
