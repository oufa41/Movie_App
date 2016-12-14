package imdb.movieapp.Adapters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Raoof on 10/31/2016.
 */
public class Movie {
    private Integer page;
    private ArrayList<MovieResult> results = new ArrayList<MovieResult>();
    private Integer total_results;
    private Integer total_pages;

    public int getPage() {
        return page;
    }

    public ArrayList<MovieResult> getResults() {
        return results;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }
}
