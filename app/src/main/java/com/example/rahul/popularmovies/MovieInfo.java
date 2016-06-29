package com.example.rahul.popularmovies;

/**
 * Created by rahul on 6/25/16.
 */
public class MovieInfo {
    private String name;
    private String pictureUrl;

    MovieInfo(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
