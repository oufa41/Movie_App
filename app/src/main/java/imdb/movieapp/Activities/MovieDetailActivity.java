package imdb.movieapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import imdb.movieapp.Adapters.VideoResult;
import imdb.movieapp.Application.AppController;
import imdb.movieapp.Fragments.MovieDetailFragment;
import imdb.movieapp.R;

public class MovieDetailActivity extends AppCompatActivity {
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Detail");

        movieDetailFragment = new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_movies_detail, movieDetailFragment)
                .commit();
        final String movieID = getIntent().getStringExtra("movie_id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_trailer) {
               Toast.makeText(getApplicationContext(),"share",Toast.LENGTH_SHORT).show();
        } else {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
