package imdb.movieapp.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import imdb.movieapp.Adapters.MovieResult;
import imdb.movieapp.Adapters.ReviewsRecyclerViewAdapter;
import imdb.movieapp.Adapters.ReviewsResults;
import imdb.movieapp.Adapters.VideoResult;
import imdb.movieapp.Adapters.VideosRecyclerViewAdapter;
import imdb.movieapp.Application.AppController;
import imdb.movieapp.R;


public class MovieDetailFragment extends Fragment {
    private RecyclerView recyclerViewVideos, recyclerViewReviews;
    private LinearLayout movieDetail, movieOverview, movieFullDetail, layoutFavorite;
    private ImageView posterView;
    private TextView movieNameView, movieReleaseDateView, movieVoteAverageView, moviewOverviewView, movieFavorite;
    private String movieId, movieName, moviePosterPath, movieReleaseDate, movieAverageVotes, movieOverviewString;
    private ImageButton favoriteIcon;
    private AppController appController;
    private MovieResult movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    private boolean isFavoriteMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        appController = (AppController) getActivity().getApplication();
        Intent intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            String movieResult = intent.getExtras().get("movie_results").toString();
            if (movieResult != null) {
                movie = appController.getGson().fromJson(intent.getExtras().get("movie_results").toString(), MovieResult.class);
                Log.i("oufa: ", "" + movie.getOriginal_title());
            }
        }
        if (getArguments() != null) {
            movie = appController.getGson().fromJson(getArguments().getString("movie_results"), MovieResult.class);
            movieId = getArguments().getString("movie_id");
            movieName = getArguments().getString("movie_name");
            moviePosterPath = getArguments().getString("movie_poster");
            movieReleaseDate = getArguments().getString("movie_release_date");
            movieAverageVotes = getArguments().getString("movie_average_votes");
            movieOverviewString = getArguments().getString("movie_overview");
        }

        ArrayList<MovieResult> favoritelist = appController.getFavorites();
        if (favoritelist != null) {
            for (int i = 0; i < favoritelist.size(); i++) {
                if (favoritelist.get(i).getId() == movie.getId())
                    isFavoriteMovie = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieFullDetail = (LinearLayout) view.findViewById(R.id.movie_full_detail);
        recyclerViewVideos = (RecyclerView) movieFullDetail.findViewById(R.id.recycler_view_videos);
        recyclerViewReviews = (RecyclerView) movieFullDetail.findViewById(R.id.recycler_view_reviews);
        layoutFavorite = (LinearLayout) view.findViewById(R.id.layout_favorite);
        movieFavorite = (TextView) layoutFavorite.findViewById(R.id.text_view_movie_favorite);
        favoriteIcon = (ImageButton) layoutFavorite.findViewById(R.id.image_button_movie_favorite);
        if (isFavoriteMovie) {
            favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on);
            movieFavorite.setText("Favorite");
        }
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFavoriteLists();
            }
        });

        Intent intent = getActivity().getIntent();


        if (intent.getStringExtra("movie_id") != null) {
            buildMovieDetailViewIntent(view, intent);

            videosRequest(movieId);
            reviewsRequest(movieId);
        } else {
            buildMovieDetailViewArguments(view);
            videosRequest(movieId);
            reviewsRequest(movieId);
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void buildMovieDetailViewIntent(View view, Intent intent) {
        movieDetail = (LinearLayout) view.findViewById(R.id.movie_detail);
        posterView = (ImageView) movieDetail.findViewById(R.id.movie_poster_detail);
        movieNameView = (TextView) movieDetail.findViewById(R.id.text_view_movie_name);
        movieReleaseDateView = (TextView) movieDetail.findViewById(R.id.text_view_release_date);
        movieVoteAverageView = (TextView) movieDetail.findViewById(R.id.text_view_vote_average);
        movieOverview = (LinearLayout) view.findViewById(R.id.movie_overview);
        moviewOverviewView = (TextView) movieOverview.findViewById(R.id.text_view_movie_overview);

         movieId= intent.getStringExtra("movie_id");
        final String movieName = intent.getStringExtra("movie_name");
        movieNameView.setText(movieName);
        final String moviePosterPath = intent.getStringExtra("movie_poster");
        String moviePosterCompleteURL = AppController.MOVIE_POSTER_URL + moviePosterPath;
        Picasso.with(getContext()).load(moviePosterCompleteURL).into(posterView);
        final String movieReleaseDate = intent.getStringExtra("movie_release_date");
        movieReleaseDateView.append(": " + movieReleaseDate);
        final String movieAverageVotes = intent.getStringExtra("movie_average_votes");
        movieVoteAverageView.append(": " + movieAverageVotes + "/10");
        final String movieOverviewString = intent.getStringExtra("movie_overview");
        moviewOverviewView.setText(movieOverviewString);


    }

    public void buildMovieDetailViewArguments(View view) {
        movieDetail = (LinearLayout) view.findViewById(R.id.movie_detail);
        posterView = (ImageView) movieDetail.findViewById(R.id.movie_poster_detail);
        movieNameView = (TextView) movieDetail.findViewById(R.id.text_view_movie_name);
        movieReleaseDateView = (TextView) movieDetail.findViewById(R.id.text_view_release_date);
        movieVoteAverageView = (TextView) movieDetail.findViewById(R.id.text_view_vote_average);
        movieOverview = (LinearLayout) view.findViewById(R.id.movie_overview);
        moviewOverviewView = (TextView) movieOverview.findViewById(R.id.text_view_movie_overview);


        movieNameView.setText(movieName);
        String moviePosterCompleteURL = AppController.MOVIE_POSTER_URL + moviePosterPath;
        Picasso.with(getContext()).load(moviePosterCompleteURL).into(posterView);
        movieReleaseDateView.append(": " + movieReleaseDate);
        movieVoteAverageView.append(": " + movieAverageVotes + "/10");
        moviewOverviewView.setText(movieOverviewString);


    }

    public void videosRequest(String movieID) {
        if (!isOnline()) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            //  StringRequest stringRequestMovies = new StringRequest(Request.Method.GET, AppController.MOVIES_API_URL,
            String videoURL = AppController.MOVIE_URL + movieID + AppController.VIDEOS + AppController.API_KEY;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    videoURL, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray results = jObj.getJSONArray("results");
                        Log.i("Results Videos: ", results.toString());
                        Gson gson = new Gson();
                        ArrayList<VideoResult> videos = gson.fromJson(results.toString(), new TypeToken<ArrayList<VideoResult>>() {
                        }.getType());
                        updateVideosView(videos);
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

    public void updateVideosView(ArrayList<VideoResult> videos) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewVideos.setLayoutManager(mLayoutManager);
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setAdapter(new VideosRecyclerViewAdapter(videos, getContext()));
    }

    public void reviewsRequest(String movieID) {
        if (!isOnline()) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            //  StringRequest stringRequestMovies = new StringRequest(Request.Method.GET, AppController.MOVIES_API_URL,
            String reviewURL = AppController.MOVIE_URL + movieID + AppController.REVIEWS + AppController.API_KEY;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    reviewURL, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray results = jObj.getJSONArray("results");
                        Log.i("Results Views: ", results.toString());
                        Gson gson = new Gson();
                        ArrayList<ReviewsResults> reviews = gson.fromJson(results.toString(), new TypeToken<ArrayList<ReviewsResults>>() {
                        }.getType());
                        updateReviewsView(reviews);
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

    public void updateReviewsView(ArrayList<ReviewsResults> reviews) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewReviews.setLayoutManager(mLayoutManager);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReviews.setAdapter(new ReviewsRecyclerViewAdapter(reviews, getContext()));
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkFavoriteLists() {

        Log.i("isFavorite", "" + isFavoriteMovie);
        if (isFavoriteMovie) {
            Toast.makeText(getActivity(), "Already exist in favorites", Toast.LENGTH_SHORT).show();
        } else {
            movieFavorite.setText("Favorite");
            favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            appController.addFavorite(movie);
            isFavoriteMovie = true;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_detail_fragment, menu);
        MenuItem share = menu.findItem(R.id.share_trailer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_trailer) {
            /*if (!isOnline()) {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                //  StringRequest stringRequestMovies = new StringRequest(Request.Method.GET, AppController.MOVIES_API_URL,
                String videoURL = AppController.MOVIE_URL + movieId + AppController.VIDEOS + AppController.API_KEY;
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        videoURL, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray results = jObj.getJSONArray("results");
                            Log.i("Results Videos: ", results.toString());
                            Gson gson = new Gson();
                            ArrayList<VideoResult> videos = gson.fromJson(results.toString(), new TypeToken<ArrayList<VideoResult>>() {
                            }.getType());
                            Intent appIntent = new Intent(Intent.ACTION_MEDIA_SHARED, Uri.parse("vnd.youtube:" + videos.get(0).getKey()));

                            Intent webIntent = new Intent(Intent.ACTION_MEDIA_SHARED, Uri.parse("http://www.youtube.com/watch?v=" + videos.get(0).getKey()));
                            try {
                                getActivity().startActivity(appIntent);
                            } catch (ActivityNotFoundException ex) {
                                getActivity().startActivity(webIntent);
                            }
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
            }*/
        }
        return super.onOptionsItemSelected(item);
    }
}
