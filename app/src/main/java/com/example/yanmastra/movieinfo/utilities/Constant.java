package com.example.yanmastra.movieinfo.utilities;

import com.example.yanmastra.movieinfo.BuildConfig;

/**
 * Created by Yan Mastra on 8/17/2017.
 */

public class Constant {
    public static final String API_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String PARAM_API_KEY = "?api_key=";
    public static final String POPULAR = "popular";
    public static final String MOVIE_KEY = "movies";
    public static final String MOVIE_ID = "";
    public static final String VIDEOS = "/videos";
    public static final String REVIEWS = "/reviews";
    public static final String TOP_RATED = "top_rated";
    public static final String LATEST = "latest";
    public static final String UP_COMING = "upcoming";
    public static final String NOW_PLAYING = "now_playing";
    public static final String FAVORITES = "favorites";
    public static final String SELECTED_CATEGORY = "selected_category";
    public static final String LAYOUT_MANAGER = "layout_manager";

    public static String getMovieGenre(int id){
        switch(id){
            case 28 : return "Action";
            case 12 : return "Adventure";
            case 16 : return "Animation";
            case 35 : return "Comedy";
            case 80 : return "Crime";
            case 99 : return "Documentary";
            case 18 : return "Drama";
            case 10751 : return "Family";
            case 14 : return "Fantasy";
            case 36 : return "History";
            case 27 : return "Horror";
            case 10402 : return "Music";
            case 9648 : return "Mystery";
            case 10749 : return "Romance";
            case 878 : return "Science Fiction";
            case 10770 : return "TV Movie";
            case 53 : return "Thriller";
            case 10752 : return "War";
            case 37 : return "Western";
            default:return "Unknown genre";
        }
    }
}
