package imdb.movieapp.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import imdb.movieapp.R;

/**
 * Created by Raoof on 11/1/2016.
 */
public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ReviewsResults> reviews;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewTitle;
        public TextView reviewContent;

        public MyViewHolder(View view) {
            super(view);
            reviewTitle = (TextView) view.findViewById(R.id.text_view_review_title);
            reviewContent = (TextView) view.findViewById(R.id.text_view_review_content);

        }
    }

    public ReviewsRecyclerViewAdapter(ArrayList<ReviewsResults> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviews, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ReviewsResults reviewsResults = reviews.get(position);
        holder.reviewTitle.setText(reviewsResults.getAuthor());
        holder.reviewContent.setText(reviewsResults.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
