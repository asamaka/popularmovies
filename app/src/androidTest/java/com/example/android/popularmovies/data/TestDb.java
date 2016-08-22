package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;


public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(FavoriteMoviesDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FavoriteMoviesContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(FavoriteMoviesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FavoriteMoviesDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the table",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FavoriteMoviesContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(FavoriteMoviesContract.MovieEntry._ID);
        locationColumnHashSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID);
        locationColumnHashSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE);
        locationColumnHashSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }


    public void testTable() {
        insert();
    }

    public long insert() {

        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createFakeMovieCV();

        long locationRowId;
        locationRowId = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        assertTrue(locationRowId != -1);

        Cursor cursor = db.query(
                FavoriteMoviesContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        cursor.close();
        db.close();
        return locationRowId;
    }
}
