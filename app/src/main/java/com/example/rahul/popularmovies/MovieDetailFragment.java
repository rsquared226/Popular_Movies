package com.example.rahul.popularmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_BUNDLE = "movieDetail";
    MovieInfo movieInfo;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieInfo = getActivity().getIntent().getExtras().getParcelable(MOVIE_BUNDLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdrop_image);
        Picasso.with(getContext()).load(movieInfo.getBackdropPath()).into(backdrop); // Load image from web
        backdrop.setContentDescription(movieInfo.getName() + " backdrop");

        return rootView;
    }

}
