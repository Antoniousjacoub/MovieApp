package com.example.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.View.Movie;
import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Tony on 4/1/2016.
 */
public class MovieGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final Context movieContext;


    ArrayList<Movie> moviesList;

    public MovieGridAdapter(Context context, ArrayList<Movie> movieArrayList) {
        movieContext = context;
        this.moviesList = movieArrayList;

    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Movie getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder = null;
        Movie movies = moviesList.get(position);
        System.out.println("Size Adapter " + moviesList.size());
        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) movieContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.grid_item_movie, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.grid_item_image);

            viewHolder.txt_backdropImage = (TextView) rowView.findViewById(R.id.txt_backdrop);
            viewHolder.txt_id = (TextView) rowView.findViewById(R.id.txt_id);
            viewHolder.txt_Language = (TextView) rowView.findViewById(R.id.txt_Language);
            viewHolder.txt_tite = (TextView) rowView.findViewById(R.id.txt_title);
            viewHolder.txt_VotingNumber = (TextView) rowView.findViewById(R.id.txt_votingNumber);
            viewHolder.txt_releaseDate = (TextView) rowView.findViewById(R.id.txt_releaseDate);
            viewHolder.txt_votecount = (TextView) rowView.findViewById(R.id.txt_votecount);
            viewHolder.txt_overview = (TextView) rowView.findViewById(R.id.txt_overview);
            viewHolder.txt_imageurl = (TextView) rowView.findViewById(R.id.txt_imageURL);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String imageURL = movies.getImage();
        viewHolder.txt_VotingNumber.setText(movies.getRating());
        viewHolder.txt_tite.setText(movies.getTitle());
        viewHolder.txt_Language.setText(movies.getLanguage());
        viewHolder.imageView.setTag(movies.getImage());
        viewHolder.txt_id.setText(String.valueOf(movies.getId()));
        viewHolder.txt_overview.setText(movies.getOverview());
        viewHolder.txt_releaseDate.setText(movies.getRelease_date());
        viewHolder.txt_votecount.setText(movies.getVote_count());
        viewHolder.txt_imageurl.setText(imageURL);
        Picasso.with(movieContext).load(movies.getImage()).into(viewHolder.imageView);
        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView txt_id;
        public TextView txt_tite;
        public TextView txt_backdropImage;
        public TextView txt_Language;
        public TextView txt_VotingNumber;

        public TextView txt_releaseDate;
        public TextView txt_votecount;
        public TextView txt_overview;
        public TextView txt_imageurl;


    }

}
