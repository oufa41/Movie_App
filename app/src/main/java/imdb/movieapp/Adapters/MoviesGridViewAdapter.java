package imdb.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import imdb.movieapp.Application.AppController;
import imdb.movieapp.R;

/**
 * Created by Raoof on 10/23/2016.
 */
public class MoviesGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> moviePostersPaths;
    public MoviesGridViewAdapter(Context context) {
        this.context = context;

    }
    public MoviesGridViewAdapter(Context context, ArrayList<String> moviePostersPaths) {
        this.context = context;
        this.moviePostersPaths = moviePostersPaths;
    }

    @Override
    public int getCount() {
        return moviePostersPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_movie, parent, false);
        }
        ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_poster);
        String moviePosterCompleteURL = AppController.MOVIE_POSTER_URL + moviePostersPaths.get(position);
        Picasso.with(context).load(moviePosterCompleteURL).into(posterView);
    /*    Picasso.with(context)
                .load(moviePosterCompleteURL)
                .placeholder(R.drawable.)
                .error(R.drawable.user_placeholder_error)
                .into(posterView);*/
        return convertView;
    }
}
