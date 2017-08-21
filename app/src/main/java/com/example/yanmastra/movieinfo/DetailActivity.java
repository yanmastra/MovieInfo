package com.example.yanmastra.movieinfo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yanmastra.movieinfo.adapter.ReviewAdapter;
import com.example.yanmastra.movieinfo.adapter.TrailerAdapter;
import com.example.yanmastra.movieinfo.model.movies.MovieResults;
import com.example.yanmastra.movieinfo.model.review.Reviews;
import com.example.yanmastra.movieinfo.model.review.ReviewResults;
import com.example.yanmastra.movieinfo.model.trailer.Trailer;
import com.example.yanmastra.movieinfo.model.trailer.TrailerResults;
import com.example.yanmastra.movieinfo.utilities.Constant;
import com.example.yanmastra.movieinfo.utilities.DateFormatter;
import com.example.yanmastra.movieinfo.utilities.ImageUrlBuilder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Gson gson = new Gson();
    private String jsonData;
    private String movieId;
    private MovieResults movieResults;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.cl_parent_detail) CoordinatorLayout parentDetail;
    @BindView(R.id.iv_backdrop) ImageView ivBackDrop;
    @BindView(R.id.iv_detail_poster) ImageView ivPoster;
    @BindView(R.id.tv_gendre) TextView tvGendre;
    @BindView(R.id.tv_releas_date) TextView tvReleasDate;
    @BindView(R.id.tv_vote_average) TextView tvVoteAverage;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.tv_ori_language) TextView oriLanguage;

    @BindView(R.id.rv_reviews) RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;
    private List<ReviewResults> reviewResultses = new ArrayList<>();

    @BindView(R.id.rv_trailers) RecyclerView rvTrailer;
    private TrailerAdapter trailerAdapter;
    private List<TrailerResults> trailerResultses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        jsonData = getIntent().getStringExtra(Constant.MOVIE_KEY);

        if(jsonData != null){
            movieResults = gson.fromJson(jsonData, MovieResults.class);
            bindData();
            trailerRecyclerView();
            reviewRecyclerView();
        }else{
            Log.e(TAG, "Data is null");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void trailerRecyclerView(){
        trailerAdapter = new TrailerAdapter(trailerResultses);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.setHasFixedSize(true);
        rvTrailer.setAdapter(trailerAdapter);
        movieId = getIntent().getStringExtra(Constant.MOVIE_ID);
        getTrailerFromAPI(movieId);
    }
    private void getTrailerFromAPI(String movieId){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.API_URL + movieId + Constant.VIDEOS + Constant.PARAM_API_KEY + Constant.API_KEY;
        Log.e(TAG, "ini url TRailer "+url);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Trailer trailer = gson.fromJson(response, Trailer.class);
                            for (TrailerResults result : trailer.getResults()) {
                                trailerResultses.add(result);
                            }
                            trailerAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Log.e(TAG, error.getMessage());
                        }else{
                            Log.e(TAG, "Something error hapended");
                        }
                    }
                }
        );
        requestQueue.add(stringRequest);
        Log.e(TAG, "ini trailer string request " + stringRequest);
    }

    private void reviewRecyclerView(){
        reviewAdapter = new ReviewAdapter(reviewResultses);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReview.setHasFixedSize(true);
        movieId = getIntent().getStringExtra(Constant.MOVIE_ID);
        getReviewFromAPI(movieId);
        rvReview.setAdapter(reviewAdapter);
        rvReview.setNestedScrollingEnabled(false);
    }
    private void getReviewFromAPI(String movieId){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.API_URL + movieId + Constant.REVIEWS + Constant.PARAM_API_KEY + Constant.API_KEY;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Reviews reviews = gson.fromJson(response, Reviews.class);
                            for (ReviewResults result : reviews.getResults()) {
                                reviewResultses.add(result);
                                Log.e(TAG, "Content req: "+result.getContent());
                            }
                            trailerAdapter.notifyDataSetChanged();
                            Log.e(TAG, "ini reviews "+response);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage()+"ini error ");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Log.e(TAG, error.getMessage());
                        }else{
                            Log.e(TAG, "Something error hapended");
                        }
                    }
                }
        );
        requestQueue.add(stringRequest);
        Log.e(TAG, "ini string request "+stringRequest);
    }

    private void bindData(){
        parentDetail.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(movieResults.getTitle());
        Picasso.with(this)
                .load(ImageUrlBuilder.getBackdropUrl(movieResults.getBackdrop_path()))
                .placeholder(R.drawable.ic_local_movie)
                .error(R.drawable.ic_error)
                .into(ivBackDrop);
        Picasso.with(this)
                .load(ImageUrlBuilder.getPosterUrl(movieResults.getPoster_path()))
                .placeholder(R.drawable.ic_local_movie)
                .error(R.drawable.ic_error)
                .into(ivPoster);
        tvReleasDate.setText(DateFormatter.getReadableDate(movieResults.getRelease_date()));
        tvVoteAverage.setText(String.valueOf(movieResults.getVote_average()));
        tvOverview.setText(movieResults.getOverview());
        oriLanguage.setText(movieResults.getOriginal_language());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
