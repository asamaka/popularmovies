package com.example.android.popularmovies;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by asser on 8/3/16.
 */

public class MovieData implements Serializable{
    public String title;
    public String poster_url;
    public String overview;
    public double vote_average;
    public double popularity;
    public Calendar release_date;
}
