package com.example.yanmastra.movieinfo.model.trailer;

import java.util.List;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class Trailer {
    private int id;
    private List<TrailerResults> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerResults> getResults() {
        return results;
    }

    public void setResults(List<TrailerResults> trailerResultsList) {
        this.results = trailerResultsList;
    }
}
