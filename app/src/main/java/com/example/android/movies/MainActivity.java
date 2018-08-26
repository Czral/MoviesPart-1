package com.example.android.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String BASE = "https://api.themoviedb.org/3";
    private static final String DISCOVER = "discover";
    private static final String MOVIE = "movie";
    private static final String SEARCH = "search";
    private static final String FIND = "find";
    private static final String QUERY = "query";
    private static final String API = "api_key";

    // TODO:Insert API KEY here:
    private static final String API_KEY = "";
    private static final String PAGE = "page";

    Uri movieUrl = null;

    @BindView(R.id.empty_state_text_view)
    TextView emptyStateTextView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.grid_view)
    GridView listView;

    ArrayList<ImageMovie> arrayList;

    GridAdapter adapter;

    int i = 1;

    private boolean connection;

    // double voteAverageMovie;
    // String overviewMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        listView.setEmptyView(emptyStateTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connection = networkInfo != null && networkInfo.isConnected();

        if (connection) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String keyword = sharedPreferences.getString("keyword", "");

            if (keyword.isEmpty()) {
                movieUrl = Uri.parse(BASE).
                        buildUpon().
                        appendPath(DISCOVER).
                        appendPath(MOVIE).
                        appendQueryParameter(API, API_KEY).
                        appendQueryParameter(PAGE, String.valueOf(i)).
                        build();

            } else {

                movieUrl = Uri.parse(BASE).
                        buildUpon().
                        appendPath(SEARCH).appendPath(MOVIE).
                        appendQueryParameter(QUERY, keyword).
                        appendQueryParameter(API, API_KEY).
                        appendQueryParameter(PAGE, String.valueOf(i)).
                        build();
            }

            new FetchMovieData().execute(movieUrl.toString());
        } else {

            emptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    public class FetchMovieData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                getResponseFromHttpUrl(Uri.parse(strings[0]));

                return getResponseFromHttpUrl(Uri.parse(strings[0]));

            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
        }

        @Override
        protected void onPostExecute(String strings) {
            progressBar.setVisibility(View.INVISIBLE);
            getDataFromJson(strings);
            super.onPostExecute(strings);
        }

    }

    private String getResponseFromHttpUrl(Uri uri) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        URL url = new URL(uri.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        try {
            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();

                }
            }

        } finally {

            httpURLConnection.disconnect();
            inputStream.close();
        }
        return stringBuilder.toString();
    }

    public void getDataFromJson(String string) {

        arrayList = new ArrayList();
        try {

            JSONObject currentMovie = new JSONObject(string);
            JSONArray resultArray = currentMovie.getJSONArray("results");

            for (int i = 0; i <= resultArray.length(); i++) {

                JSONObject movie = resultArray.getJSONObject(i);
                String idMovie = movie.getString("backdrop_path");
                String titleMovie = movie.getString("original_title");
                String imageMovie = movie.getString("poster_path");
                double voteAverageMovie = movie.getDouble("vote_average");
                String overviewMovie = movie.getString("overview");
                final String releaseDateMovie = movie.getString("release_date");

                arrayList.add(new ImageMovie(idMovie, titleMovie, imageMovie, voteAverageMovie, overviewMovie));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ImageMovie movie = (ImageMovie) adapter.getItem(position);
                        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);

                        String path = movie.getMoviePath();
                        String title = movie.getName();
                        String overview = movie.getOverview();
                        double average = movie.getVoteAverage();
                        String image = movie.getImageUrl();

                        detailIntent.putExtra("RELEASE", releaseDateMovie);
                        detailIntent.putExtra("PATH", path);
                        detailIntent.putExtra("TITLE", title);
                        detailIntent.putExtra("IMAGE", image);
                        detailIntent.putExtra("OVERVIEW", overview);
                        detailIntent.putExtra("VOTEAVERAGE", average);

                        startActivity(detailIntent);

                    }
                });

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

        adapter = new GridAdapter(this, arrayList);

        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.sort_by:
                if (connection) {


                }

            case R.id.next_page:
                if (connection) {
                    i += 1;
                    Uri upMovieUri = Uri.parse(BASE).
                            buildUpon().
                            appendPath(DISCOVER).
                            appendPath(MOVIE).
                            appendQueryParameter(API, API_KEY).
                            appendQueryParameter(PAGE, String.valueOf(i)).
                            build();
                    new FetchMovieData().execute(upMovieUri.toString());
                    return true;
                } else {

                    emptyStateTextView.setVisibility(View.VISIBLE);
                    return true;
                }
            case R.id.previous_page:
                if (connection) {
                    i -= 1;
                    Uri upMovieUri = Uri.parse(BASE).
                            buildUpon().
                            appendPath(DISCOVER).
                            appendPath(MOVIE).
                            appendQueryParameter(API, API_KEY).
                            appendQueryParameter(PAGE, String.valueOf(i)).
                            build();
                    new FetchMovieData().execute(upMovieUri.toString());
                    return true;
                } else {

                    emptyStateTextView.setVisibility(View.VISIBLE);
                    return true;

                }

            case R.id.search:
                if (connection) {

                    Intent keywordIntent = new Intent(MainActivity.this, MainSettingsActivity.class);
                    startActivity(keywordIntent);
                    return true;

                } else {

                    emptyStateTextView.setVisibility(View.VISIBLE);
                    return true;
                }

       //     case R.id.genre:



        }

        return super.onOptionsItemSelected(item);
    }


    public static class GenreFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.genre_settings);
        }
    }




}