package com.example.android.movies;

/**
 * Created by XXX on 25-Jun-18.
 */

public class ImageMovie {

    String moviePath;
    String name;
    String imageUrl;
    double voteAverage;
    String overview;

    public ImageMovie(String path, String name1, String imageURL, double voteAverage1, String overView) {

        moviePath = path;
        name = name1;
        imageUrl = imageURL;
        voteAverage = voteAverage1;
        overview = overView;
    }

    public String getMoviePath() {

        return moviePath;
    }

    public String getName() {

        return name;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public double getVoteAverage() {

        return voteAverage;
    }

    public String getOverview() {

        return overview;
    }




}
