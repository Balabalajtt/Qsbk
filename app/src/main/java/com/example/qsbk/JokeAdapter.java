package com.example.qsbk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/9.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {

    private List<Joke> mJokeList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView jokeText;
        private ImageView jokeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            jokeText = (TextView) itemView.findViewById(R.id.joke_text);
            jokeImage = (ImageView) itemView.findViewById(R.id.joke_image);
        }
    }

    public JokeAdapter(List<Joke> jokeList, Context context) {
        mJokeList = jokeList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joke_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(JokeAdapter.ViewHolder holder, int position) {
        Joke joke = mJokeList.get(position);
        holder.jokeText.setText(joke.getJokeText());
        Log.d("555222", "onBindViewHolder: " + mJokeList.size());
        if (joke.getJokeImageUrl() != null) {
            holder.jokeImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(joke.getJokeImageUrl()).into(holder.jokeImage);
        } else {
            holder.jokeImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }


}
