package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MovieData> movies;
    private MovieListAdapter rvAdapter;

    public enum SortBy {
        POPULARITY, RATING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
