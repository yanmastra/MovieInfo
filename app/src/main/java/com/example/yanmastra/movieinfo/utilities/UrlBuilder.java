package com.example.yanmastra.movieinfo.utilities;

/**
 * Created by Yan Mastra on 8/19/2017.
 */

public class UrlBuilder {
    private static final String TMDB_URL = "http://image.tmdb.org/t/p/";
    private static final String YOUTUBE_URL = "http://img.youtube.com/vi/";
    public static String getPosterUrl(String path){return TMDB_URL + "w92"+path;}
    public static String getMovieItemsUrl(String path){return TMDB_URL + "w185"+path;}
    public static String getBackdropUrl(String path){return TMDB_URL + "w300"+path;}
    public static String getTrailerThumbnailUrl(String key){return YOUTUBE_URL + key + "/0.jpg";}
    public static String getTrailerYoutubeUrl(String key){return "http://www.youtube.com/watch?v="+key;}
}
