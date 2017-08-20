package com.example.yanmastra.movieinfo.model.review;

import java.util.List;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class Reviews {
    private int id;
    private int page;
    private List<ReviewResults> results;
    private int total_pages;
    private int total_results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ReviewResults> getResults() {
        return results;
    }

    public void setResults(List<ReviewResults> reviewResultsList) {
        this.results = reviewResultsList;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
