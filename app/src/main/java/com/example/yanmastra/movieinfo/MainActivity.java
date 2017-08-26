package com.example.yanmastra.movieinfo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.example.yanmastra.movieinfo.database.FavoriteContract;
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
    private String categorySelected;

    @BindView(R.id.network_retry) LinearLayout llNetworkRetry;
    private Parcelable layoutManagerSaveState;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private Cursor favorite = null;

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

        if(isNetworkConnected() || isWifiConnected()){
            if(savedInstanceState != null){
                selectedCategory = savedInstanceState.getString(Constant.SELECTED_CATEGORY);
                if(!selectedCategory.equals(Constant.FAVORITES)){getDataFromAPI(selectedCategory);}
                setSubtitle(selectedCategory);
                layoutManagerSaveState = savedInstanceState.getParcelable(Constant.LAYOUT_MANAGER);
            }else {
                selectedCategory = Constant.POPULAR;
                setSubtitle(Constant.POPULAR);
            }
            if(selectedCategory.equals(Constant.FAVORITES)){
                categorySelected = selectedCategory;
                setupLoader(this, getContentResolver());
                restartLoader(getSupportLoaderManager());
            }else{
                loadData(selectedCategory);
            }
        }else {
            rvMovie.setVisibility(View.INVISIBLE);
            llNetworkRetry.setVisibility(View.VISIBLE);
        }

    }

    private void setupLoader(final MainActivity mainActivity, final ContentResolver contentResolver){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(mainActivity) {
                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return contentResolver.query(
                                    FavoriteContract.Entry.CONTENT_URI,
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
                        if(categorySelected.equals(Constant.FAVORITES)){
                            forceLoad();
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.d("Favorite found : ", ""+data.getCount());
                favorite = data;
                generateFromCursor(favorite);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

    private void generateFromCursor(Cursor cursor) {
        List<MovieResults> results = new ArrayList<>();
        cursor.moveToPosition(-1);
        try {
            while(cursor.moveToNext()){
                MovieResults movieResults = new MovieResults();
                movieResults.setPoster_path(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_POSTER))
                );
                movieResults.setTitle(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_TITLE))
                );
                movieResults.setRelease_date(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_RELEASE_DATE))
                );
                movieResults.setOverview(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_OVERVIEW))
                );
                movieResults.setVote_average(
                        cursor.getDouble(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_RATING))
                );
                movieResults.setId(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_MOVIE_ID))
                );
                movieResults.setBackdrop_path(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_BACKDROP))
                );
                movieResults.setGenre_ids(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_GENRE))
                );
                movieResults.setOriginal_language(
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_LANGUAGE))
                );
                Log.w(TAG, "genre ids : "+cursor.getString(cursor.getColumnIndex(FavoriteContract.Entry.COLUMN_GENRE)));
                results.add(movieResults);
            }
        }finally {
            cursor.close();
        }
        onDataReceived(results, 1);
    }

    private void onDataReceived(List<MovieResults> results, int page) {
        Log.w(TAG, "dat receive : "+results);
        movieAdapter.replaceAll(results);
        if(layoutManagerSaveState != null){
            rvMovie.getLayoutManager().onRestoreInstanceState(layoutManagerSaveState);
        }
    }

    private void restartLoader(LoaderManager supportLoaderManager) {
        supportLoaderManager.restartLoader(Constant.LOADER_MAIN_ID, null, loaderCallbacks);
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
                            //Log.e(TAG, error.getMessage());
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
                setSubtitle(selectedCategory);
                categorySelected = selectedCategory;
                setupLoader(this, getContentResolver());
                Log.w(TAG, "when select Favorite "+getContentResolver());
                restartLoader(getSupportLoaderManager());
                return true;
            default: return super.onOptionsItemSelected(item);
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
