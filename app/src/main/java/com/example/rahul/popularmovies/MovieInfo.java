package com.example.rahul.popularmovies;

import java.util.Date;

/**
 * Created by rahul on 6/25/16.
 */
public class MovieInfo {
    private String name;
    private String pictureUrl;
    private String overview;
    private float rating;
    private Date release;

    public MovieInfo(String name, String pictureUrl, String overview, float rating, Date release) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.overview = overview;
        this.rating = rating;
        this.release = release;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getOverview() {
        return overview;
    }

    public float getRating() {
        return rating;
    }

    public Date getRelease() {
        return release;
    }
}
