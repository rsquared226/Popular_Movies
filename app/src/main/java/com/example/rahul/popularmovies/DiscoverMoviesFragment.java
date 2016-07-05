package com.example.rahul.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    // MOVIE_INFO_KEY is for writing and reading from Parcelables
    private static final String MOVIE_INFO_KEY = "movieInfos";
    private static final String LOG_TAG = "DiscoverMoviesFragment";

    GridView movieGrid;
    private ArrayList<MovieInfo> movieInfos;
    private MovieInfoAdapter movieInfoAdapter;

    public DiscoverMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_INFO_KEY)) {
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

        if (movieInfos == null) { // If moviesInfos was not in the savedInstanceState bundle, then access TMDB data
            if (isOnline()) {
                (rootView.findViewById(R.id.image_offline)).setVisibility(View.GONE);
                new DownloadMovieData().execute(POPULAR);
            }
        } else { // If it was in the bundle, set it as the adapter
            setMovieAdapter(movieInfos);
        }

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movieDetail", movieInfoAdapter.getItem(position)));
            }
        });

        return rootView;
    }

    private void setMovieAdapter(ArrayList<MovieInfo> movieInfos) {
        // Makes adapter based on movieInfos ArrayList
        movieInfoAdapter = new MovieInfoAdapter(getActivity(), movieInfos);
        movieGrid.setAdapter(movieInfoAdapter);

        for (MovieInfo movieInfo :
                movieInfos) {
            Log.v(LOG_TAG, movieInfo.toString());
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class DownloadMovieData extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        private ArrayList<MovieInfo> parseData(String movieJsonStr) throws JSONException {
            final String RESULTS = "results";
            final String POSTER = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASE = "release_date";
            final String TITLE = "original_title";
            final String RATING = "vote_average";
            final String BACKDROP = "backdrop_path";

            JSONArray movieArray = new JSONObject(movieJsonStr).getJSONArray(RESULTS);
            movieInfos = new ArrayList<>(movieArray.length());

            // This loop adds a MovieInfo object to the ArrayAdapter
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieData = movieArray.getJSONObject(i);

                movieInfos.add(new MovieInfo(movieData.getString(TITLE),
                        movieData.getString(POSTER),
                        movieData.getString(OVERVIEW),
                        movieData.getDouble(RATING),
                        movieData.getString(RELEASE),
                        movieData.getString(BACKDROP)));
            }

            return movieInfos;
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null; // Can't do anything without knowing which type of movies user wants
            }

            // String from the TMDB API
            String movieJsonStr;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie";
                final String APP_ID_PARAM = "api_key";
                final String API_KEY = getApiKey();

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

                Log.v(LOG_TAG, movieJsonStr);
            } catch (PackageManager.NameNotFoundException ex) {
                Log.e(LOG_TAG, "API Key not found");
                return null; // Nothing can be done if the api key was not found
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
                return null; // Didn't get data
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return parseData(movieJsonStr);
            } catch (JSONException ex) {
                Log.e(LOG_TAG, ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            setMovieAdapter(movieInfos);
        }

        private String getApiKey() throws PackageManager.NameNotFoundException {
            ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("API_KEY");
        }
    }
}
