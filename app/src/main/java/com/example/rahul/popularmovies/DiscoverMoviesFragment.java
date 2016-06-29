package com.example.rahul.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverMoviesFragment extends Fragment {

    private MovieInfoAdapter movieInfoAdapter;

    public DiscoverMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover_movies, container, false);

        MovieInfo[] movieInfos = {
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new MovieInfo("Interstellar", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
        };

        movieInfoAdapter = new MovieInfoAdapter(getActivity(), Arrays.asList(movieInfos));

        GridView movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        movieGrid.setAdapter(movieInfoAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }


}
