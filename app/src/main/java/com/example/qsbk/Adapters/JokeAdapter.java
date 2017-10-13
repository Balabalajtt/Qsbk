package com.example.qsbk.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qsbk.Beans.Joke;
import com.example.qsbk.R;
import com.example.qsbk.Utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/9.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {

    private List<Joke> mJokeList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView jokeText;
        private ImageView jokeImage;
        private TextView jokeStatus;
        private ImageView authorImage;
        private TextView authorName;

        public ViewHolder(View itemView) {
            super(itemView);
            jokeText = (TextView) itemView.findViewById(R.id.joke_text);
            jokeImage = (ImageView) itemView.findViewById(R.id.joke_image);
            jokeStatus = (TextView) itemView.findViewById(R.id.joke_status);
            authorName = (TextView) itemView.findViewById(R.id.author_name);
            authorImage = (ImageView) itemView.findViewById(R.id.head_picture);
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
    public void onBindViewHolder(final JokeAdapter.ViewHolder holder, final int position) {
        Joke joke = mJokeList.get(position);
        holder.jokeText.setText(joke.getJokeText());
        holder.jokeStatus.setText(joke.getJokeStatus());
        holder.authorName.setText(joke.getJokeAuthor());
        Glide.with(mContext).load(joke.getHeadPhotoUrl()).transform(new GlideCircleTransform(mContext)).into(holder.authorImage);
        if (joke.getJokeImageUrl() != null) {
            holder.jokeImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(joke.getJokeImageUrl()).into(holder.jokeImage);
        } else {
            holder.jokeImage.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
