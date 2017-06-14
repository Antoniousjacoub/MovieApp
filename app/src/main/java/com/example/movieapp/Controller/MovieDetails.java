package com.example.movieapp.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Adapters.ReviewAdapter;
import com.example.movieapp.Adapters.TrailerAdapter;
import com.example.movieapp.R;
import com.example.movieapp.View.Movie;
import com.example.movieapp.View.Review;
import com.example.movieapp.View.Trailer;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

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
import java.util.List;


public class MovieDetails extends AppCompatActivity {

    public static final String TAG = MovieDetails.class.getSimpleName();

    private LinearListView mTrailersView;
    private LinearListView mReviewsView;

    private CardView mReviewsCardview;
    private CardView mTrailersCardview;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Trailer mTrailer;
    private ShareActionProvider mShareActionProvider;
    private Movie mMovie;
    private Toast mToast;
    String title, votingNumber, releasedate, overview, imageURL, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView txt_title2 = (TextView) findViewById(R.id.txt_title2);
        TextView txt_votingNumber2 = (TextView) findViewById(R.id.txt_votingNumber2);
        ImageView img_drop = (ImageView) findViewById(R.id.img_drop2);
        TextView txt_overiew = (TextView) findViewById(R.id.txt_overview2);
        TextView txt_releasedate = (TextView) findViewById(R.id.txt_releaseDate2);


        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        votingNumber = intent.getStringExtra("votingNumber");
        releasedate = intent.getStringExtra("releasedate");
        overview = intent.getStringExtra("overview");
        imageURL = intent.getStringExtra("imageURL");

        Picasso.with(this).load(imageURL).into(img_drop);
        txt_title2.setText(title);
        txt_votingNumber2.setText(votingNumber);
        txt_overiew.setText(overview);
        txt_releasedate.setText(releasedate);


        mMovie = new Movie(Integer.parseInt(id), title, releasedate, "", overview, "", imageURL, votingNumber);

        mTrailersView = (LinearListView) findViewById(R.id.detail_trailers);
        mReviewsView = (LinearListView) findViewById(R.id.detail_reviews);

        mReviewsCardview = (CardView) findViewById(R.id.detail_reviews_cardview);
        mTrailersCardview = (CardView) findViewById(R.id.detail_trailers_cardview);

        mTrailerAdapter = new TrailerAdapter(MovieDetails.this, new ArrayList<Trailer>());
        mTrailersView.setAdapter(mTrailerAdapter);


        mTrailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Trailer trailer = mTrailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);
            }
        });
        mReviewAdapter = new ReviewAdapter(MovieDetails.this, new ArrayList<Review>());
        mReviewsView.setAdapter(mReviewAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovie != null) {
            new FetchTrailersTask().execute(Integer.toString(mMovie.getId()));
            new FetchReviewsTask().execute(Integer.toString(mMovie.getId()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        final MenuItem action_favorite = menu.findItem(R.id.action_star);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return IsFavoriteMovie(mMovie.getId());
            }

            @Override
            protected void onPostExecute(Integer isFavorited) {
                action_favorite.setIcon(isFavorited == 1 ? R.drawable.rating_star_on : R.drawable.rating_star_off);
            }
        }.execute();

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                if (mMovie != null) {

                    Intent shareintent = new Intent(Intent.ACTION_SEND);
                    shareintent.setType("text/plain");
                    shareintent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + " " + "http://www.youtube.com/watch?v=" + mTrailer.getKey());
                    startActivity(Intent.createChooser(shareintent, "Share via"));
                }
                return true;
            case R.id.action_star:
                if (mMovie != null) {
                    int isFavorite = IsFavoriteMovie(mMovie.getId());
                    if ((isFavorite == 1)) {
                        FavoriteMovies.DeleteFavoriteMovieById(getApplicationContext(), mMovie.getId());
                        item.setIcon(R.drawable.rating_star_off);
                        Toast.makeText(getApplicationContext(), R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<Movie> movieArrayList = FavoriteMovies.loadFavorites(getApplicationContext());
                        if ((movieArrayList == null))
                            movieArrayList = new ArrayList<>();
                        movieArrayList.add(mMovie);
                        FavoriteMovies.DeleteFavoriteMovies(getApplicationContext());
                        FavoriteMovies.storeFavorites(getApplicationContext(), movieArrayList);
                        item.setIcon(R.drawable.rating_star_on);
                        Toast.makeText(getApplicationContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();

                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Integer IsFavoriteMovie(int id) {
        ArrayList<Movie> movieArrayList = FavoriteMovies.loadFavorites(getApplicationContext());
        if ((movieArrayList != null)) {

            for (int i = 0; i < movieArrayList.size(); i++) {
                Movie movie = movieArrayList.get(i);
                if (movie.getId() == id)
                    return 1;
            }
            return 0;
        } else
            return 0;
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                }
            }

            return results;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
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
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override

        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    mTrailersCardview.setVisibility(View.VISIBLE);
                    if (mTrailerAdapter != null) {
                        mTrailerAdapter.clear();
                        for (Trailer trailer : trailers) {
                            mTrailerAdapter.add(trailer);
                        }
                    }

                    mTrailer = trailers.get(0);
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }


    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }


    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Review> results = new ArrayList<>();

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                results.add(new Review(review));
            }

            return results;
        }

        @Override
        protected List<Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
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
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                if (reviews.size() > 0) {
                    mReviewsCardview.setVisibility(View.VISIBLE);
                    if (mReviewAdapter != null) {
                        mReviewAdapter.clear();
                        for (Review review : reviews) {
                            mReviewAdapter.add(review);
                        }
                    }
                }
            }
        }
    }


}
