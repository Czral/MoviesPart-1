package com.example.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by XXX on 25-Jun-18.
 */

public class GridAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ImageMovie> imageMovieArrayList;

    public GridAdapter(Context context, ArrayList<ImageMovie> imageMovies) {

        mContext = context;
        imageMovieArrayList = imageMovies;
    }

    @Override
    public int getCount() {
        return imageMovieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageMovieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (gridView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = layoutInflater.inflate(R.layout.list_item, null);
        }

        ImageView imageView = (ImageView) gridView.findViewById(R.id.image_view_list);
  //      TextView textView = (TextView) gridView.findViewById(R.id.text_view_list);

        if (imageMovieArrayList.get(position) == null || imageMovieArrayList.get(position).getImageUrl() == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
         //   textView.setText(imageMovieArrayList.get(position).getName());
            Picasso.get().
                    load("http://image.tmdb.org/t/p/w500/" + imageMovieArrayList.get(position).getImageUrl())
                    .error(R.mipmap.ic_launcher).into(imageView);




        }
        return gridView;
    }
}
