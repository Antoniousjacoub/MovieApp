package com.example.movieapp.Controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;

/**
 * Created by Toni on 4/23/2016.
 */
public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    public DetailFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragement,
                container, false);

        return view;
    }
}
