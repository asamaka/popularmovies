package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.android.popularmovies.data.FavoriteMoviesContract;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 * Created by asser on 8/19/16.
 */
public class TestUtilities {

    public static MovieData createFakeMovie() {
        MovieData md = new MovieData();
        md.id= 12345;
        md.title="I'm a fake movie";
        md.poster_url = "http://image.tmdb.org/t/p/w780/cGOPbv9wA5gEejkUN892JrveARt.jpg";
        md.popularity = 10.101;
        md.release_date = Calendar.getInstance();
        md.vote_count = 9999;
        md.vote_average = 8.7;
        md.overview = "What a great movie!!";
        return md;
    }


    public static ContentValues createFakeMovieCV() {
        MovieData md = createFakeMovie();
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID,md.id);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE,md.title);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_URL,md.poster_url);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY,md.popularity);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,md.vote_average);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT,md.vote_count);
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_YEAR,md.release_date.toString());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW,md.overview);
        return cv;
    }


    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

}
