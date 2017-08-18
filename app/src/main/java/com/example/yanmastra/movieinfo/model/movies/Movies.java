package com.example.yanmastra.movieinfo.model.movies;

import java.util.List;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class Movies {
    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieResults> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_result) {
        this.total_results = total_result;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_page) {
        this.total_pages = total_page;
    }

    public List<MovieResults> getResults() {
        return results;
    }

    public void setResults(List<MovieResults> results) {
        this.results = results;
    }
}
