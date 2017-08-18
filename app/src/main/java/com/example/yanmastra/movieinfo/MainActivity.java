package com.example.yanmastra.movieinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.yanmastra.movieinfo.adpter.MovieAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_movie) RecyclerView rvMovie;
    private List<String> data = new ArrayList<>();
    private MovieAdapter movieAdapter;

    private Gson gson = new Gson();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setHasFixedSize(true);
        addingData();
        movieAdapter = new MovieAdapter(data);
        rvMovie.setAdapter(movieAdapter);
    }
    private List<String> addingData(){
        for (int i=1; i<=10; i++){
            this.data.add("Movie Items "+i);
        }
        return this.data;
    }
}
