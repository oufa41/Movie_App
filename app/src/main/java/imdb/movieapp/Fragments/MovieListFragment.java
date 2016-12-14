package imdb.movieapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

import imdb.movieapp.Activities.MovieDetailActivity;
import imdb.movieapp.Adapters.MovieResult;
import imdb.movieapp.Adapters.MoviesGridViewAdapter;
import imdb.movieapp.Application.AppController;
import imdb.movieapp.R;

public class MovieListFragment extends Fragment {

    private GridView moviesGridView;
    private MoviesGridViewAdapter moviesGridViewAdapter;
    private OnMovieListener onMovieListener;
    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        moviesGridView = (GridView) view.findViewById(R.id.grid_view_movies);
        moviesGridViewAdapter = new MoviesGridViewAdapter(getActivity());
        Log.i("results", "gridview");
        //moviesRequest();
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onMovieListener = (OnMovieListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void updateMoviesView(final ArrayList<MovieResult> movieResults) {

        ArrayList<String> moviePostersPaths = new ArrayList<>();
        for (int i = 0; i < movieResults.size(); i++) {
            moviePostersPaths.add(movieResults.get(i).getPoster_path());
        }


        moviesGridViewAdapter = new MoviesGridViewAdapter(getActivity(), moviePostersPaths);
        moviesGridView.setAdapter(moviesGridViewAdapter);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMovieListener.onMovieSelected(movieResults.get(position));
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void moviesRequest() {
        if (!isOnline()) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            //  StringRequest stringRequestMovies = new StringRequest(Request.Method.GET, AppController.MOVIES_API_URL,
            String url = AppController.MOVIES_API_URL + "top_rated" + AppController.API_KEY;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray results = jObj.getJSONArray("results");
                        Gson gson = new Gson();
                        ArrayList<MovieResult> movies = gson.fromJson(results.toString(), new TypeToken<ArrayList<MovieResult>>() {
                        }.getType());
                        updateMoviesView(movies);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
            AppController.getInstance().addToRequestQueue(strReq);
        }
    }

    public interface OnMovieListener {
        void onMovieSelected(MovieResult movie);
    }

    public OnMovieListener getmListener() {
        return onMovieListener;
    }

    public void setmListener(MovieListFragment.OnMovieListener onMovieListener) {
        this.onMovieListener= onMovieListener;
    }
}
