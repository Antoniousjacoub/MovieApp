package com.example.movieapp.Controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.movieapp.Adapters.MovieGridAdapter;
import com.example.movieapp.R;
import com.example.movieapp.View.Movie;

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
 * Created by Toni on 4/23/2016.
 */
public class MyListFragment extends Fragment {
    GridView mGridView;
    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private Movie mMovie;
    MovieGridAdapter mMovieGridAdapter;
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_count.desc";
    private static final String FAVORITE = "favorite";


    private String mSortBy = POPULARITY_DESC;
    public static final String TAG = DetailFragment.class.getSimpleName();

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.action_sort_by_favorite);

        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(FAVORITE)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_favorite.setChecked(true);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = POPULARITY_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = RATING_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = FAVORITE;
                updateMovies(mSortBy);
                return true;
            default:

                return true;
        }
    }
    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals(FAVORITE)) {
            new FetchMovies(getActivity()).execute(sort_by);
        }
else {
            mMovieGridAdapter = new MovieGridAdapter(getActivity(), FavoriteMovies.loadFavorites(getActivity()));
            mGridView.setAdapter(mMovieGridAdapter);
            mMovieGridAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSortBy = POPULARITY_DESC;
        updateMovies(mSortBy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragement, container);
        mGridView = (GridView) root.findViewById(R.id.gridview_movies);

        FetchMovies movieTask = new FetchMovies(getActivity());
        movieTask.execute();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(MyListFragment.DETAIL_MOVIE);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if (MainActivity.isTablet()==true) {
                  //Toast.makeText(getActivity(), "true", Toast.LENGTH_LONG).show();

                  Movie movie = mMovieGridAdapter.getItem(position);
                  ((Callback) getActivity()).onItemSelected(movie);
              }
                if (MainActivity.isTablet()==false){
                    //Toast.makeText(getActivity(),"22222", Toast.LENGTH_LONG).show();

                    TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
                    TextView txt_language = (TextView) view.findViewById(R.id.txt_Language);
                    TextView txt_votingNumber = (TextView) view.findViewById(R.id.txt_votingNumber);
                    TextView txt_overview = (TextView) view.findViewById(R.id.txt_overview);
                    TextView txt_releaseDate = (TextView) view.findViewById(R.id.txt_releaseDate);
                    TextView txt_votingcount = (TextView) view.findViewById(R.id.txt_votecount);
                    TextView txt_imageURL = (TextView) view.findViewById(R.id.txt_imageURL);
                    TextView txt_id = (TextView) view.findViewById(R.id.txt_id);

                    Intent intent = new Intent(getActivity(), MovieDetails.class);

                    intent.putExtra("id", txt_id.getText());
                    intent.putExtra("title", txt_title.getText());
                    intent.putExtra("imageURL", txt_imageURL.getText());
                    intent.putExtra("language", txt_language.getText());
                    intent.putExtra("votingNumber", txt_votingNumber.getText());
                    intent.putExtra("overview", txt_overview.getText());
                    intent.putExtra("releasedate", txt_releaseDate.getText());
                    intent.putExtra("votecount", txt_votingcount.getText());

                    startActivity(intent);
                }
            }
        });
        return  root;
    }


    public void onFinishDataLoading(ArrayList<Movie> movies) {

        mMovieGridAdapter = new MovieGridAdapter(getActivity(), movies);
        mGridView.setAdapter(mMovieGridAdapter);
        mMovieGridAdapter.notifyDataSetChanged();

    }

       public  class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        private Context context;

        public FetchMovies(Context context) {
            this.context = context;
        }

        private ArrayList<Movie> getMoviesDataFromJson(String jsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            ArrayList<Movie> results = new ArrayList<>();

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie movieModel = new Movie(movie);
                results.add(movieModel);
            }

            return results;
        }


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

//           String moviesURL = params[0];
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.app_api))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
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
                return getMoviesDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;


        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {

                onFinishDataLoading(movies);
            }
        }

    }
}
