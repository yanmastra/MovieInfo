package com.example.yanmastra.movieinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridLayoutColumns(this));
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setHasFixedSize(true);
        //addingData();
        movieAdapter = new MovieAdapter(data, this);
        rvMovie.setAdapter(movieAdapter);
        movieAdapter.replaceAll(data);
        getDataFromAPI(Constant.POPULAR);
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
    public void onItemClick(MovieResults data, int position) {
        Intent startDetailactivity = new Intent(this, DetailActivity.class);
        startDetailactivity.putExtra(Constant.MOVIE_KEY, gson.toJson(data));
        startDetailactivity.putExtra(Constant.MOVIE_ID, String.valueOf(data.getId()));
        startActivity(startDetailactivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
