package com.example.rahul.popularmovies;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rahul on 6/25/16.
 */
public class MovieInfo {
    private String name;
    private String posterPath;
    private String overview;
    private double rating;
    private Date release;

    public MovieInfo(String name, String posterPath, String overview, double rating, String release) {
        this.name = name;
        this.posterPath = getCompletePosterPath(posterPath);
        this.overview = overview;
        this.rating = rating;
        this.release = parseDate(release);
    }

    private String getCompletePosterPath(String posterPath) {
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String SIZE = "w342"; //looks okay on N6, with fast loading
        return BASE_URL + SIZE + posterPath;
    }

    private Date parseDate(String release) {
        final String DATE_FORMAT = "yyyy-MM-dd";
        try {
            return new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(release);
        } catch (ParseException ex) {
            Log.e(getClass().toString(), "Release Date could not be parsed");
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public Date getRelease() {
        return release;
    }

    @Override
    public String toString() {
        return getName() + "\n" +
                getPosterPath() + "\n" +
                getRating() + "\n" +
                getRelease();
    }
}
