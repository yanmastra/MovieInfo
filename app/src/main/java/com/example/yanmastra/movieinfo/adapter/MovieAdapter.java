package com.example.yanmastra.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yanmastra.movieinfo.R;
import com.example.yanmastra.movieinfo.model.movies.MovieResults;
import com.example.yanmastra.movieinfo.utilities.UrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yan Mastra on 8/17/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieResults> data = new ArrayList<>();
    private ItemClickListener onClickListener;

    public interface ItemClickListener{
        void onItemClick(MovieResults data, int position);
    }

    public MovieAdapter(List<MovieResults> data, ItemClickListener onClickListener) {
        this.data = data;
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_items, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(data.get(position), position);
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
        public void bind(final MovieResults data, final int position){
            //txt_movie_items.setText(data.getTitle());
            Picasso.with(itemView.getContext())
                    .load(UrlBuilder.getMovieItemsUrl(data.getPoster_path()))
                    .placeholder(R.drawable.ic_local_movie)
                    .error(R.drawable.ic_error).into(img_movie_items);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(data, position);
                }
            });
        }
    }
    public void replaceAll(List<MovieResults> list){
        this.data.clear();
        this.data = list;
        notifyDataSetChanged();
    }
}
