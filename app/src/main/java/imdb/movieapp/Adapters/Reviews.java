package imdb.movieapp.Adapters;

import java.util.ArrayList;

/**
 * Created by Raoof on 11/1/2016.
 */
public class Reviews {
    private Integer id;
    private Integer page;
    private ArrayList<ReviewsResults> results = new ArrayList<ReviewsResults>();
    private Integer total_pages;
    private Integer total_results;

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public ArrayList<ReviewsResults> getResults() {
        return results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }
}
