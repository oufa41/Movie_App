package imdb.movieapp.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import imdb.movieapp.Adapters.MovieResult;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    public static final String MOVIES_API_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=" + "your api key";
    public static final String MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String VIDEOS = "/videos";
    public static final String REVIEWS = "/reviews";
    public static final String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    @Override
    public void onCreate() {
        super.onCreate();
        getRequestQueue();
        mInstance = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public Gson getGson() {
        return gson;
    }

    public void addFavorite(MovieResult movie) {

        ArrayList<MovieResult> favoritesList = getFavorites();
        if (favoritesList == null)
            favoritesList = new ArrayList<MovieResult>();


        if (!favoritesList.contains(movie)) {
            favoritesList.add(movie);
            saveFavorites(favoritesList);
        }
    }


    public ArrayList<MovieResult> getFavorites() {

        ArrayList<MovieResult> favoritesList;
        if (sharedPreferences.contains("favorite_movie")) {

            String favoritesJSON = sharedPreferences.getString("favorite_movie", null);
            favoritesList = getGson().fromJson(favoritesJSON, new TypeToken<ArrayList<MovieResult>>() {
            }.getType());

        } else
            return null;

        return favoritesList;
    }

    private void saveFavorites(ArrayList<MovieResult> favoritesList) {

        String favoritesJSON = getGson().toJson(favoritesList);
        editor.putString("favorite_movie", favoritesJSON);
        editor.commit();
    }

}