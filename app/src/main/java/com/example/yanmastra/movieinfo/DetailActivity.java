package com.example.yanmastra.movieinfo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.yanmastra.movieinfo.database.FavoriteContract;
import com.example.yanmastra.movieinfo.model.movies.MovieResults;
import com.example.yanmastra.movieinfo.model.review.ReviewResults;
import com.example.yanmastra.movieinfo.model.review.Reviews;
import com.example.yanmastra.movieinfo.model.trailer.Trailer;
import com.example.yanmastra.movieinfo.model.trailer.TrailerResults;
import com.example.yanmastra.movieinfo.utilities.Constant;
import com.example.yanmastra.movieinfo.utilities.DateFormatter;
import com.example.yanmastra.movieinfo.utilities.UrlBuilder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClick{
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Gson gson = new Gson();
    private String jsonData;
    private String movieId;
    private MovieResults movieResults;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    //lanjut buat setup loader

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.cl_parent_detail) CoordinatorLayout parentDetail;
    @BindView(R.id.iv_backdrop) ImageView ivBackDrop;
    @BindView(R.id.iv_detail_poster) ImageView ivPoster;
    @BindView(R.id.tv_genre) TextView tvGenre;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_vote_average) TextView tvVoteAverage;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.tv_ori_language) TextView oriLanguage;

    @BindView(R.id.rv_reviews) RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;
    private List<ReviewResults> reviewResults = new ArrayList<>();

    @BindView(R.id.rv_trailers) RecyclerView rvTrailer;
    private TrailerAdapter trailerAdapter;
    private List<TrailerResults> trailerResults = new ArrayList<>();

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
            setupLoader(this, getContentResolver(), Long.parseLong(getMovieResults(jsonData).getId()));
            initLoader(getSupportLoaderManager());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if((Integer)fab.getTag() == R.drawable.ic_star_selected){
                        unsetAsFavorite(getContentResolver(), getMovieResults(jsonData));
                    }else{
                        saveAsFavorite(getContentResolver(), getMovieResults(jsonData));
                        restartLoader(getSupportLoaderManager());
                    }
                }
            });
        }else{
            Log.e(TAG, "Data is null");
        }
    }

    private void unsetAsFavorite(ContentResolver contentResolver, MovieResults movieResults) {
        long result = contentResolver.delete(uriWithIDBuilder(Long.parseLong(movieResults.getId())),null, null);
        if (result > 0){
            restartLoader(getSupportLoaderManager());
        }
    }
    private MovieResults getMovieResults(String json){
        return gson.fromJson(json, MovieResults.class);
    }
    private void saveAsFavorite(ContentResolver resolver, MovieResults item){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.Entry.COLUMN_MOVIE_ID, Integer.parseInt(item.getId()));
        cv.put(FavoriteContract.Entry.COLUMN_TITLE, item.getTitle());
        cv.put(FavoriteContract.Entry.COLUMN_BACKDROP, item.getBackdrop_path());
        cv.put(FavoriteContract.Entry.COLUMN_POSTER, item.getPoster_path());
        cv.put(FavoriteContract.Entry.COLUMN_RATING, item.getVote_average());
        cv.put(FavoriteContract.Entry.COLUMN_RELEASE_DATE, item.getRelease_date());
        cv.put(FavoriteContract.Entry.COLUMN_OVERVIEW, item.getOverview());
        String genre = null;
        int i =0;
        for(String gen : movieResults.getGenre_ids()){
            if(i==0){
                genre = gen;
            }else {
                genre += ", "+gen;
            }
            i++;
        }
        cv.put(FavoriteContract.Entry.COLUMN_GENRE, genre);
        cv.put(FavoriteContract.Entry.COLUMN_LANGUAGE, item.getOriginal_language());
        resolver.insert(FavoriteContract.Entry.CONTENT_URI, cv);
    }

    private void setupLoader(final DetailActivity detailActivity, final ContentResolver contentResolver, final Long movieId){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(detailActivity) {
                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return contentResolver.query(
                                    uriWithIDBuilder(movieId),
                                    null,
                                    null,
                                    null,
                                    null
                            );
                        }catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                setFavoriteButton(cursor.getCount());
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLoader(getSupportLoaderManager());
    }

    private void restartLoader(LoaderManager supportLoaderManager) {
        supportLoaderManager.restartLoader(Constant.LOADER_ID, null, loaderCallbacks);
    }
    private void initLoader(LoaderManager loaderManager){
        loaderManager.initLoader(Constant.LOADER_ID, null, loaderCallbacks);
    }
    private Uri uriWithIDBuilder(Long movieId) {
        return ContentUris.withAppendedId(FavoriteContract.Entry.CONTENT_URI, movieId);
    }
    private void setFavoriteButton(int count) {
        if(count > 0){
            onStatusReceived(true);
        }else{
            onStatusReceived(false);
        }
    }
    private void onStatusReceived(boolean isFavorite) {
        if(isFavorite){
            fab.setImageResource(R.drawable.ic_star_selected);
            fab.setTag(R.drawable.ic_star_selected);
        }else {
            fab.setImageResource(R.drawable.ic_star_unselected);
            fab.setTag(R.drawable.ic_star_unselected);
        }
    }

    private void trailerRecyclerView(){
        trailerAdapter = new TrailerAdapter(trailerResults, this);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.setHasFixedSize(true);
        rvTrailer.setAdapter(trailerAdapter);
        movieId = getIntent().getStringExtra(Constant.MOVIE_ID);
        getTrailerFromAPI(movieId);
    }
    private void getTrailerFromAPI(String movieId){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.API_URL + movieId + Constant.VIDEOS + Constant.PARAM_API_KEY + Constant.API_KEY;
        Log.e(TAG, "ini url Trailer "+url);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Trailer trailer = gson.fromJson(response, Trailer.class);
                            for (TrailerResults result : trailer.getResults()) {
                                trailerResults.add(result);
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
        reviewAdapter = new ReviewAdapter(reviewResults);
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
                                reviewResults.add(result);
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
                            Log.e(TAG, "Something error happened");
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
                .load(UrlBuilder.getBackdropUrl(movieResults.getBackdrop_path()))
                .placeholder(R.drawable.ic_local_movie)
                .error(R.drawable.ic_error)
                .into(ivBackDrop);
        Picasso.with(this)
                .load(UrlBuilder.getPosterUrl(movieResults.getPoster_path()))
                .placeholder(R.drawable.ic_local_movie)
                .error(R.drawable.ic_error)
                .into(ivPoster);
        tvReleaseDate.setText(DateFormatter.getReadableDate(movieResults.getRelease_date()));
        tvVoteAverage.setText(String.valueOf(movieResults.getVote_average()));
        tvOverview.setText(movieResults.getOverview());
        oriLanguage.setText(movieResults.getOriginal_language());
        String genre = "";
        int i=1;
        for (String genId : movieResults.getGenre_ids()) {
            if(i==1) {
                genre += Constant.getMovieGenre(genId);
            }else{
                genre += ", "+Constant.getMovieGenre(genId);
            }
            i++;
        }
        tvGenre.setText(genre);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_setting){
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT, detailInfoMovie());
            share.setType("text/plain");
            startActivity(share);
        }
        return true;
    }
    private String detailInfoMovie(){
        String data;
        String id      = "Id : "+movieResults.getId();
        String title   = "\nTitle : " + movieResults.getTitle();
        String genre = "";
        int i =0;
        for(String gen : movieResults.getGenre_ids()){
            if(i==0){
                genre += Constant.getMovieGenre(gen);
            }else {
                genre += ", "+Constant.getMovieGenre(gen);
            }
            i++;
        }
        String info    = "";
        info+= "\nGenre ids : "+genre;
        info+= "\nRelease Date : "+movieResults.getRelease_date();
        info+= "\nRating : "+movieResults.getVote_average();
        info+= "\nLanguage : "+movieResults.getOriginal_language();
        String overview = "\nOverview :\n"+movieResults.getOverview();
        data = id+title+info+overview;
        return data;
    }

    @Override
    public void onTrailerClick(TrailerResults results, int position) {
        Uri video = Uri.parse(UrlBuilder.getTrailerYoutubeUrl(results.getKey()));
        Intent intent = new Intent(Intent.ACTION_VIEW, video);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
