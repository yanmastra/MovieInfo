package com.example.yanmastra.movieinfo.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yanmastra.movieinfo.R;
import com.example.yanmastra.movieinfo.model.movies.MovieResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yan Mastra on 8/17/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private Context context;
    private List<MovieResults> data = new ArrayList<>();

    public MovieAdapter(List<MovieResults> data) {
        this.data = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_items, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_movie_items) ImageView img_movie_items;
        //@BindView(R.id.tv_movie_items) TextView txt_movie_items;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(MovieResults data){
            //txt_movie_items.setText(data.getTitle());
            Picasso.with(itemView.getContext())
                    .load("http://image.tmdb.org/t/p/w185"+data.getPoster_path())
                    .placeholder(R.drawable.ic_local_movie)
                    .error(R.drawable.ic_error).into(img_movie_items);
        }
    }
    public void replaceAll(List<MovieResults> list){
        this.data.clear();
        this.data = list;
        notifyDataSetChanged();
    }
}
