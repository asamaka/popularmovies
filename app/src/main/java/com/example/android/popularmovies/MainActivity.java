package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.data.FavoriteMoviesProvider;
import com.example.android.popularmovies.sync.FetchMovieDataAsync;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.sync.MovieListAdapter;
import com.example.android.popularmovies.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private List<MovieData> movies;
    private MovieListAdapter rvAdapter;
    private Context context;

    public enum SortBy {
        POPULARITY, RATING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        RecyclerView rv = (RecyclerView) findViewById(R.id.all_movies_list);

        rv.setLayoutManager(new GridLayoutManager(this, 4));
        movies = new ArrayList<>();
        rvAdapter = new MovieListAdapter(this, movies);
        rv.setAdapter(rvAdapter);
        new FetchMovieDataAsync().execute(this, SortBy.POPULARITY);
    }


    /**
     * Called when the FetchDataAsyncTask is complete
     *
     * @param m new list of movies
     */
    public void updateMovies(List<MovieData> m) {
        movies.clear();
        movies.addAll(m);
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
                return true;
            case R.id.sort_by_votes_menu_item:
                new FetchMovieDataAsync().execute(this, SortBy.RATING);
                return true;
            case R.id.sort_by_favorites:
                showFavorites();
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
