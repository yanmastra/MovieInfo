package com.example.yanmastra.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yanmastra.movieinfo.R;
import com.example.yanmastra.movieinfo.model.trailer.TrailerResults;
import com.example.yanmastra.movieinfo.utilities.ImageUrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yan Mastra on 8/18/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    List<TrailerResults> data = new ArrayList<>();

    public TrailerAdapter(List<TrailerResults> data) {
        this.data = data;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_items, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_trailer) ImageView ivTrailer;
        @BindView(R.id.tv_trailer_title) TextView trailerTitle;
        @BindView(R.id.tv_trailer_type) TextView trailerName;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(TrailerResults data){
            trailerName.setText(data.getName());
            trailerTitle.setText(data.getType());
            Picasso.with(itemView.getContext())
                    .load(ImageUrlBuilder.getTrailerThumbnailUrl(data.getKey()))
                    .placeholder(R.drawable.ic_local_movie)
                    .error(R.drawable.ic_error).into(ivTrailer);
        }
    }
}
