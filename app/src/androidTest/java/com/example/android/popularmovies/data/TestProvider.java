package com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by asser on 8/21/16.
 */

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from table during delete", 0, cursor.getCount());
        cursor.close();

    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FavoriteMoviesProvider.class.getName());
        try {

            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: FavoriteMoviesProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FavoriteMoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FavoriteMoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: FavoriteMoviesProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {

        String type = mContext.getContentResolver().getType(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI);

        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                FavoriteMoviesContract.MovieEntry.CONTENT_TYPE, type);

        MovieData md = TestUtilities.createFakeMovie();
        Uri uri = FavoriteMoviesContract.MovieEntry.buildFavoriteMovieUri(md.id);
        type = mContext.getContentResolver().getType(uri);

        assertEquals("Error: the CONTENT_URI with Id should return MovieEntry.CONTENT_TYPE",
                FavoriteMoviesContract.MovieEntry.CONTENT_TYPE, type);

    }


    public void testBasicFavoriteMoviesQuery() {
        // insert our test records into the database
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createFakeMovieCV();

        long rowId = db.insert(
                FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert Movie into the Database", rowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor favoriteMoviesCursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor(
                "testBasicFavoriteMoviesQuery", favoriteMoviesCursor, testValues);
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createFakeMovieCV();

        Uri uri = mContext.getContentResolver().insert(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI, testValues);

        long rowId = ContentUris.parseId(uri);

        // Verify we got a row back.
        assertTrue(rowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, testValues);

    }

}
