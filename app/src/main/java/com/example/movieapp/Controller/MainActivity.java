package com.example.movieapp.Controller;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.movieapp.R;
import com.example.movieapp.View.Movie;


public class MainActivity extends AppCompatActivity implements MyListFragment.Callback {
    Boolean mTwoPane;
 static  Boolean test;
    public static boolean isTablet(){
        return test;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            //Toast.makeText(getApplicationContext(), "m is true ", Toast.LENGTH_LONG).show();
            if (savedInstanceState == null) {

                    test=true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(),
                                DetailActivityFragment.TAG)
                        .commit();
            }
        } else {
            //Toast.makeText(getApplicationContext(), "m is false ", Toast.LENGTH_LONG).show();
            mTwoPane = false;
            test = false;
        }
    }


    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            //Toast.makeText(getApplicationContext(), "mtowpane is true", Toast.LENGTH_LONG).show();
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_MOVIE, movie);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailActivityFragment.TAG)

                    .commit();

        }
    }


}
