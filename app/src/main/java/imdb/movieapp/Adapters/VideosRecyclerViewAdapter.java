package imdb.movieapp.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import imdb.movieapp.R;

/**
 * Created by Raoof on 11/1/2016.
 */
public class VideosRecyclerViewAdapter extends RecyclerView.Adapter<VideosRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<VideoResult> videos;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton trailerPlayImageView;
        public TextView trailerNameTextView;

        public MyViewHolder(View view) {
            super(view);
            trailerPlayImageView = (ImageButton) view.findViewById(R.id.image_view_movie_trailer_play);
            trailerNameTextView = (TextView) view.findViewById(R.id.text_view_movie_trailer_name);

        }
    }

    public VideosRecyclerViewAdapter(ArrayList<VideoResult> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_videos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VideoResult videoResult = videos.get(position);
        holder.trailerNameTextView.setText(videoResult.getName());
        holder.trailerPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open from existing youtube application
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoResult.getKey()));
                // open from web youtube

                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + videoResult.getKey()));

                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


}
