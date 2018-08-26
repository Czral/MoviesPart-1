package com.example.android.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent detailIntent = getIntent();

        String Title = detailIntent.getStringExtra("TITLE");
        String OverView = detailIntent.getStringExtra("OVERVIEW");
        String Image = detailIntent.getStringExtra("IMAGE");

        String Release = detailIntent.getStringExtra("RELEASE");
        double averageVote = detailIntent.getDoubleExtra("VOTEAVERAGE", 0);
        float Average = Float.valueOf(String.valueOf(averageVote));
        String ReleaseDate = parseDate(Release);

        TextView textView = findViewById(R.id.plot_synopsis);
        textView.setMovementMethod(new ScrollingMovementMethod());
        TextView titleView = findViewById(R.id.title_movie);
        RatingBar ratingBar = findViewById(R.id.vote_rating_bar);
        ImageView imageView = findViewById(R.id.poster);
        TextView releaseView = findViewById(R.id.release_date);
        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + Image).into(imageView);
        textView.setText(OverView);
        releaseView.setText(ReleaseDate);
        ratingBar.setRating(Average);
        titleView.setText(Title);

    }

    private String parseDate(String dateString) {
        String input = "yyyy-MM-dd";
        String output = "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);
        Date date;
        String finalDate = null;
        try {
            date = dateFormat.parse(dateString);
            finalDate = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return finalDate;
    }
}
