package imdb.movieapp.Adapters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raoof on 11/1/2016.
 */
public class Video {
    private Integer id;
    private ArrayList<VideoResult> results = new ArrayList<VideoResult>();

    public Integer getId() {
        return id;
    }

    public ArrayList<VideoResult> getResults() {
        return results;
    }
}
