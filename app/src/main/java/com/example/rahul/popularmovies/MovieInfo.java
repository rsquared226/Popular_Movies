package com.example.rahul.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rahul on 6/25/16.
 */
public class MovieInfo implements Parcelable {

    private String name;
    private String posterPath;
    private String overview;
    private double rating;
    private Date release;
    private String releaseStr;

    public MovieInfo(String name, String posterPath, String overview, double rating, String release) {
        this.name = name;
        this.posterPath = getCompletePosterPath(posterPath);
        this.overview = overview;
        this.rating = rating;
        this.release = parseDate(release);
        releaseStr = release;
    }

    private MovieInfo(Parcel in) {
        name = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        release = parseDate(in.readString());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getPosterPath());
        parcel.writeString(getOverview());
        parcel.writeDouble(getRating());
        parcel.writeString(releaseStr);
    }

    public final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int i) {
            return new MovieInfo[i];
        }
    };
}
