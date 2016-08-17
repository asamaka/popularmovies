package com.example.android.popularmovies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MovieData implements Serializable {
    public long id;
    public String title;
    public String poster_url;
    public String overview;
    public double vote_average;
    public long vote_count;
    public double popularity;
    public Calendar release_date;
    public List<TrailerData> trailers = new ArrayList<>();

}
