package com.example.rahul.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rahul on 6/26/16.
 */
public class MovieInfoAdapter extends ArrayAdapter<MovieInfo> {

    public MovieInfoAdapter(Activity context, List<MovieInfo> movieInfos) {
        super(context, 0, movieInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieInfo movieInfo = getItem(position);

        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_square, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(movieInfo.getPictureUrl()).into(moviePoster); // Load image from web

        // This is for accessibility service
        moviePoster.setContentDescription(movieInfo.getName());

        return convertView;
    }
}
