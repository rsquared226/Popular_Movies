package com.example.rahul.popularmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

        ImageView backdrop = (ImageView) rootView.findViewById(R.id.detail_backdrop_image);
        Picasso.with(getContext()).load(movieInfo.getBackdropPath()).into(backdrop); // Load image from web
        backdrop.setContentDescription(movieInfo.getName() + " backdrop"); // For accessibility

        View detailsView = rootView.findViewById(R.id.details_view);

        ((TextView) detailsView.findViewById(R.id.detail_movie_name)).setText(movieInfo.getName());
        ((TextView) detailsView.findViewById(R.id.detail_movie_overview)).setText(movieInfo.getOverview());
        ((TextView) detailsView.findViewById(R.id.detail_release)).setText(movieInfo.getRelease().toLocaleString());

        final RatingBar movieRating = (RatingBar) detailsView.findViewById(R.id.detail_rating);
        movieRating.setRating((float) movieInfo.getRating());

        // If the user tries to change the rating, set it back to TMDB rating
        movieRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating((float) movieInfo.getRating());
            }
        });

        return rootView;
    }
}
