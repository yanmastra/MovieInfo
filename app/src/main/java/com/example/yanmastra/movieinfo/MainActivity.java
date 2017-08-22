package com.example.yanmastra.movieinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yanmastra.movieinfo.adapter.MovieAdapter;
import com.example.yanmastra.movieinfo.model.movies.MovieResults;
import com.example.yanmastra.movieinfo.model.movies.Movies;
import com.example.yanmastra.movieinfo.utilities.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
implements MovieAdapter.ItemClickListener{
    @BindView(R.id.rv_movie) RecyclerView rvMovie;
    private List<MovieResults> data = new ArrayList<>();
    private MovieAdapter movieAdapter;

    private Gson gson = new Gson();
    private static final String TAG = MainActivity.class.getSimpleName();

    private String selectedCategory;

    @BindView(R.id.network_retry) LinearLayout llNetworkRetry;
    private Parcelable layoutManagerSaveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridLayoutColumns(this));
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(data, this);
        rvMovie.setAdapter(movieAdapter);
        movieAdapter.replaceAll(data);
        if(isNetworkConnected() || isWifiConnected()){
            if(savedInstanceState != null){
                selectedCategory = savedInstanceState.getString(Constant.SELECTED_CATEGORY);
                getDataFromAPI(selectedCategory);
                setSubtitle(selectedCategory);
                layoutManagerSaveState = savedInstanceState.getParcelable(Constant.LAYOUT_MANAGER);
            }else {
                selectedCategory = Constant.POPULAR;
                getDataFromAPI(selectedCategory);
                setSubtitle(Constant.POPULAR);
            }
        }else {
            rvMovie.setVisibility(View.INVISIBLE);
            llNetworkRetry.setVisibility(View.VISIBLE);
        }
    }
    private int gridLayoutColumns(MainActivity mainActivity){
        DisplayMetrics displayMetrics = mainActivity.getResources().getDisplayMetrics();
        float width = displayMetrics.widthPixels /displayMetrics.density;
        int columns = (int) (width/120);
        return columns;
    }
    private void getDataFromAPI(String category){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.API_URL + category + Constant.PARAM_API_KEY + Constant.API_KEY;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Movies movies = gson.fromJson(response, Movies.class);
                            data.clear();
                            for (MovieResults items : movies.getResults()){
                                data.add(items);
                            }
                            movieAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Log.e(TAG, error.getMessage());
                        }else {
                            Log.e(TAG, "Something error happened");
                        }
                    }
                }

        );
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.SELECTED_CATEGORY, selectedCategory);
        outState.putParcelable(Constant.LAYOUT_MANAGER, rvMovie.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onItemClick(MovieResults data, int position) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startDetailActivity.putExtra(Constant.MOVIE_KEY, gson.toJson(data));
        startDetailActivity.putExtra(Constant.MOVIE_ID, String.valueOf(data.getId()));
        startActivity(startDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_most_popular :
                selectedCategory = Constant.POPULAR;
                loadData(selectedCategory);
                setSubtitle(selectedCategory);
                return true;
            case R.id.action_top_rated :
                selectedCategory = Constant.TOP_RATED;
                loadData(selectedCategory);
                setSubtitle(selectedCategory);
                return true;
            case R.id.action_up_coming :
                selectedCategory = Constant.UP_COMING;
                loadData(selectedCategory);
                setSubtitle(selectedCategory);
                return true;
            case R.id.action_now_playing :
                selectedCategory = Constant.NOW_PLAYING;
                loadData(selectedCategory);
                setSubtitle(selectedCategory);
                return true;
            case R.id.action_favorites :
                selectedCategory = Constant.FAVORITES;
                loadData(selectedCategory);
                setSubtitle(selectedCategory);
                return true;
            default: return false;
        }
    }
    private void loadData(String category){
        switch (category){
            case Constant.POPULAR :
                movieAdapter.replaceAll(data);
                getDataFromAPI(Constant.POPULAR);
                break;
            case Constant.TOP_RATED :
                movieAdapter.replaceAll(data);
                getDataFromAPI(category);
                break;
            case Constant.UP_COMING :
                movieAdapter.replaceAll(data);
                getDataFromAPI(category);
                break;
            case Constant.NOW_PLAYING :
                movieAdapter.replaceAll(data);
                getDataFromAPI(category);
                break;
            case Constant.FAVORITES :
                movieAdapter.replaceAll(data);
                break;
        }
    }
    private void setSubtitle(String selectedCategory){
        switch (selectedCategory){
            case Constant.POPULAR :
                getSupportActionBar().setSubtitle(R.string.menu_most_popular);
                break;
            case Constant.TOP_RATED :
                getSupportActionBar().setSubtitle(R.string.menu_top_rated);
                break;
            case Constant.UP_COMING :
                getSupportActionBar().setSubtitle(R.string.menu_upcoming);
                break;
            case Constant.NOW_PLAYING :
                getSupportActionBar().setSubtitle(R.string.menu_now_playing);
                break;
            case Constant.FAVORITES :
                getSupportActionBar().setSubtitle(R.string.menu_favorites);
                break;
        }
    }
    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private boolean isWifiConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && (ConnectivityManager.TYPE_WIFI == networkInfo.getType());
    }
}
