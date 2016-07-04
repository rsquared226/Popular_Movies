package com.example.rahul.popularmovies;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverMoviesFragment extends Fragment {

    private static final String MOVIE_INFO_KEY = "movieInfos";
    GridView movieGrid;
    private MovieInfoAdapter movieInfoAdapter;
    private ArrayList<MovieInfo> movieInfos;

    public DiscoverMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_INFO_KEY)) {
        } else {
            movieInfos = savedInstanceState.getParcelableArrayList(MOVIE_INFO_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_INFO_KEY, movieInfos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String POPULAR = "popular";
        final String TOP_RATED = "top_rated";

        View rootView = inflater.inflate(R.layout.fragment_discover_movies, container, false);

        movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);

        if (movieInfos == null) {
            new DownloadMovieData().execute(POPULAR);
        } else {
            if (movieInfoAdapter == null) { // If it is not initialized, initialize it
                movieInfoAdapter = new MovieInfoAdapter(getActivity(), movieInfos);
            } else { // If it is, clear it and add data again
                movieInfoAdapter.clear();
                movieInfoAdapter.addAll(movieInfos);
            }
            movieGrid.setAdapter(movieInfoAdapter); //updates adapter
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private class DownloadMovieData extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        private ArrayList<MovieInfo> parseData(String movieJsonStr) throws JSONException {
            final String RESULTS = "results";
            final String POSTER = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASE = "release_date";
            final String TITLE = "original_title";
            final String RATING = "vote_average";

            JSONArray movieArray = new JSONObject(movieJsonStr).getJSONArray(RESULTS);
            movieInfos = new ArrayList<>(movieArray.length());

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieData = movieArray.getJSONObject(i);

                movieInfos.add(new MovieInfo(movieData.getString(TITLE),
                        movieData.getString(POSTER),
                        movieData.getString(OVERVIEW),
                        movieData.getDouble(RATING),
                        movieData.getString(RELEASE)));
            }

            return movieInfos;
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null; // Can't do anything without knowing which movies user wants
            }

            String movieJsonStr;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(),
                        PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                final String API_KEY = bundle.getString("API_KEY");

                final String BASE_URL = "http://api.themoviedb.org/3/movie";
                final String APP_ID_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(strings[0]) // Either popular movies or top rated
                        .appendQueryParameter(APP_ID_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();

                Log.v(getClass().toString(), movieJsonStr);
            } catch (PackageManager.NameNotFoundException ex) {
                Log.e(getClass().toString(), "API Key not found");
                return null; // nothing can be done if the api key was not found
            } catch (IOException ex) {
                Log.e(getClass().toString(), ex.getMessage());
                return null; // didn't get weather data
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(getClass().toString(), "Error closing stream", e);
                    }
                }
            }

            try {
                return parseData(movieJsonStr);
            } catch (JSONException ex) {
                Log.e(getClass().toString(), ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            if (movieInfoAdapter == null) { // If it is not initialized, initialize it
                movieInfoAdapter = new MovieInfoAdapter(getActivity(), movieInfos);
            } else { // If it is, clear it and add data again
                movieInfoAdapter.clear();
                movieInfoAdapter.addAll(movieInfos);
            }
            movieGrid.setAdapter(movieInfoAdapter); //updates adapter

            for (MovieInfo movieInfo :
                    movieInfos) {
                Log.v(getClass().toString(), movieInfo.toString());
            }
        }
    }
}
