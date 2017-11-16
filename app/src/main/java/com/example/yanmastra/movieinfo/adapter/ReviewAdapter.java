package com.example.yanmastra.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yanmastra.movieinfo.R;
import com.example.yanmastra.movieinfo.model.review.ReviewResults;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yan Mastra on 8/18/2017.
 */

public class ReviewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private List<ReviewResults> data = new ArrayList<>();

    public ReviewAdapter(List<ReviewResults> data) {
        this.data = data;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.review_items, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReviewViewHolder) holder).bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_author) TextView author;
        @BindView(R.id.tv_review) TextView review;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(ReviewResults result){
            this.author.setText(result.getAuthor());
            this.review.setText(result.getContent());
            Log.e(TAG, "ini Adapter content : "+result.getContent());
        }
    }
}
