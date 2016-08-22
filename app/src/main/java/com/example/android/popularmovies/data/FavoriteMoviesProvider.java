package com.example.android.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class FavoriteMoviesProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIES_WITH_ID = 101;
    private FavoriteMoviesDbHelper mOpenHelper;


    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case FAVORITE_MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_MOVIES_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                retCursor =  mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        null,
                        sFavoriteMovieSelection,
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITE_MOVIES:
                return FavoriteMoviesContract.MovieEntry.CONTENT_TYPE;
            case FAVORITE_MOVIES_WITH_ID:
                return FavoriteMoviesContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri returnUri;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITE_MOVIES :
                long _id = mOpenHelper.getWritableDatabase().
                        insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = FavoriteMoviesContract.MovieEntry.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITE_MOVIES : //delete all
                if ( null == selection )
                    selection = "1";
                rowsDeleted = db.delete(
                    FavoriteMoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIES_WITH_ID : // delete movie with id
                String movieId = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        sFavoriteMovieSelection, new String[]{movieId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated;

            switch (match) {
                case FAVORITE_MOVIES:
                    rowsUpdated = db.update(
                            FavoriteMoviesContract.MovieEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                case FAVORITE_MOVIES_WITH_ID:
                    rowsUpdated = db.update(
                            FavoriteMoviesContract.MovieEntry.TABLE_NAME, values,
                            sFavoriteMovieSelection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteMoviesContract.PATH_MOVIE, FAVORITE_MOVIES);
        matcher.addURI(authority, FavoriteMoviesContract.PATH_MOVIE + "/#", FAVORITE_MOVIES_WITH_ID);
        return matcher;
    }

    //location.location_setting = ?
    private static final String sFavoriteMovieSelection =
            FavoriteMoviesContract.MovieEntry.TABLE_NAME+
                    "." + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID + " = ? ";

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
