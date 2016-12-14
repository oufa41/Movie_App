package imdb.movieapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import imdb.movieapp.Adapters.MovieResult;
import imdb.movieapp.Application.AppController;
import imdb.movieapp.Fragments.MovieDetailFragment;
import imdb.movieapp.Fragments.MovieListFragment;
import imdb.movieapp.R;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieListener {
    private MovieListFragment movieListFragment = new MovieListFragment();
    private static String sortingType = "popular";
    private boolean tabletLayout;
    private AppController appController;
    private FrameLayout moviewDetailFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {


        }
        moviewDetailFrame = (FrameLayout) findViewById(R.id.container_movies_detail);
        if (moviewDetailFrame != null) {
            Log.i("welcome", "not null");
            tabletLayout = true;
        }
        appController = (AppController) getApplication();
        movieListFragment = new MovieListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_movies_list, movieListFragment)
                .commit();
        Log.i("oufa:", "1");
        moviesRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Movies");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.most_popular) {
            if (sortingType != "popular") {
                sortingType = "popular";
                moviesRequest();
                Toast.makeText(getApplicationContext(), "Most Popular Movies", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (itemID == R.id.top_rated) {
            if (sortingType != "top_rated") {
                sortingType = "top_rated";
                moviesRequest();
                Toast.makeText(getApplicationContext(), "Top Rated Movies", Toast.LENGTH_SHORT).show();

            }
            return true;
        } else if (itemID == R.id.favorites) {
            if (sortingType != "favorites") {
                sortingType = "favorites";

                ArrayList<MovieResult> favorites = appController.getFavorites();
                if (favorites != null)
                    movieListFragment.updateMoviesView(favorites);
                else
                    Toast.makeText(MainActivity.this, "No Favorites Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "favorites", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sortingType", sortingType);


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.get("sortingType") == "favorites") {

            ArrayList<MovieResult> favorites = appController.getFavorites();
            if (favorites != null)
                movieListFragment.updateMoviesView(favorites);
            else
                Toast.makeText(MainActivity.this, "No Favorites Found", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void moviesRequest() {
        Log.i("oufa:", "2");
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (sortingType == "favorites") {
                return;
            }
            String url = AppController.MOVIES_API_URL + sortingType + AppController.API_KEY;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray results = jObj.getJSONArray("results");
                        Log.i("Results: ", results.toString());
                        Gson gson = new Gson();
                        Log.i("oufa:", "3");
                        ArrayList<MovieResult> movies = gson.fromJson(results.toString(), new TypeToken<ArrayList<MovieResult>>() {
                        }.getType());
                        Log.i("oufa:", "4");
                        movieListFragment.updateMoviesView(movies);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
            AppController.getInstance().addToRequestQueue(strReq);
        }
    }


    @Override
    public void onMovieSelected(MovieResult movie) {
        if (!tabletLayout) {

            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            Toast.makeText(getApplicationContext(), movie.getOriginal_title(), Toast.LENGTH_SHORT).show();
            intent.putExtra("movie_name", movie.getOriginal_title());
            intent.putExtra("movie_poster", movie.getPoster_path());
            intent.putExtra("movie_release_date", movie.getRelease_date());
            intent.putExtra("movie_average_votes", "" + movie.getVote_average());
            intent.putExtra("movie_overview", movie.getOverview());
            intent.putExtra("movie_id", "" + movie.getId());
            intent.putExtra("movie_results", appController.getGson().toJson(movie));
            startActivity(intent);
        } else {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movie_name", movie.getOriginal_title());
            bundle.putString("movie_poster", movie.getPoster_path());
            bundle.putString("movie_release_date", movie.getRelease_date());
            bundle.putString("movie_average_votes", "" + movie.getVote_average());
            bundle.putString("movie_overview", movie.getOverview());
            bundle.putString("movie_id", "" + movie.getId());
            bundle.putString("movie_results", appController.getGson().toJson(movie));
            movieDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_movies_detail, movieDetailFragment).commit();
        }
    }


}
