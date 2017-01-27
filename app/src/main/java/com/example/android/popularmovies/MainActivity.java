package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.sync.FetchMovieDataAsync;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.sync.MovieListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private List<MovieData> movies;
    private MovieListAdapter rvAdapter;
    private Context context;
    private int mCurrentSort;
    private static final String STATE_SORT = "current_sort";
    private static final String STATE_GRID = "current_grid_position";
    private GridLayoutManager mLayoutManager;
    private Parcelable mGridState;

    public enum SortBy {
        POPULARITY, RATING, FAVORITES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        RecyclerView rv = (RecyclerView) findViewById(R.id.all_movies_list);
        mLayoutManager = new GridLayoutManager(this, 4);
        rv.setLayoutManager(mLayoutManager);
        movies = new ArrayList<>();
        rvAdapter = new MovieListAdapter(this, movies);
        rv.setAdapter(rvAdapter);
        if(savedInstanceState == null) {
            new FetchMovieDataAsync().execute(this, SortBy.POPULARITY);
            setTitle(getString(R.string.most_popular_title));
            mCurrentSort = 0;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current sorting state
        savedInstanceState.putInt(STATE_SORT, mCurrentSort);
        // Save list state
        mGridState = mLayoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(STATE_GRID, mGridState);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState == null) return;

        if(savedInstanceState.getInt(STATE_SORT)==1){
            new FetchMovieDataAsync().execute(this, SortBy.RATING);
            setTitle(getString(R.string.top_rated_title));
            mCurrentSort = 1;
        }
        else if(savedInstanceState.getInt(STATE_SORT)==2){
            showFavorites();
            setTitle(getString(R.string.favorites_title));
            mCurrentSort = 2;
        }else{
            new FetchMovieDataAsync().execute(this, SortBy.POPULARITY);
            setTitle(getString(R.string.most_popular_title));
            mCurrentSort = 0;
        }

        mGridState = savedInstanceState.getParcelable(STATE_GRID);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(mCurrentSort==2){
            //update favorites in case one has been removed
            showFavorites();
        }
    }


    /**
     * Called when the FetchDataAsyncTask is complete
     *
     * @param m new list of movies
     */
    public void updateMovies(List<MovieData> m) {
        movies.clear();
        movies.addAll(m);
        if (mGridState != null) {
            mLayoutManager.onRestoreInstanceState(mGridState);
        }
        rvAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular_menu_item:
                new FetchMovieDataAsync().execute(this, SortBy.POPULARITY);
                setTitle(getString(R.string.most_popular_title));
                mCurrentSort = 0;
                return true;
            case R.id.sort_by_votes_menu_item:
                new FetchMovieDataAsync().execute(this, SortBy.RATING);
                setTitle(getString(R.string.top_rated_title));
                mCurrentSort = 1;
                return true;
            case R.id.sort_by_favorites:
                showFavorites();
                setTitle(getString(R.string.favorites_title));
                mCurrentSort = 2;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showFavorites() {

        movies.clear();
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,null,null,null);
        while(c.moveToNext()){
            MovieData md = new MovieData();
            md.id = c.getLong(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID_INDEX);
            md.title = c.getString(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE_INDEX);
            md.poster_url = c.getString(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_URL_INDEX);
            md.overview = c.getString(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW_INDEX);
            md.popularity = c.getLong(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY_INDEX);
            md.vote_average = c.getDouble(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE_INDEX);
            md.vote_count = c.getLong(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT_INDEX);
            md.release_date = new GregorianCalendar();
            md.release_date.set(Calendar.YEAR,c.getInt(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_YEAR_INDEX));
            movies.add(md);
        }
        rvAdapter.notifyDataSetChanged();

    }

}
