package com.example.qsbk.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qsbk.Beans.Comment;
import com.example.qsbk.Beans.JokeDetail;
import com.example.qsbk.R;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/12.
 */

public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE {
        ITEM_TYPE_JOKE,
        ITEM_TYPE_COMMENT
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final JokeDetail mJokeDetail;
    private List<Comment> mCommentList;

    private String jokeImageUrl = null;
    private String jokeText = null;
    private String jokeStatus = null;


    public MultipleItemAdapter(Context context, JokeDetail jokeDetail) {
        mJokeDetail = jokeDetail;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mCommentList = mJokeDetail.getCommentList();
        jokeText = mJokeDetail.getJokeText();
        jokeImageUrl = mJokeDetail.getJokeImage();
        jokeStatus = mJokeDetail.getJokeStatus();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_JOKE.ordinal()) {
            return new JokeHolder(mLayoutInflater.inflate(R.layout.joke_item, parent, false));
        } else {
            return new CommentHolder(mLayoutInflater.inflate(R.layout.comment_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JokeHolder) {
            ((JokeHolder) holder).mTextView.setText(jokeText);
            ((JokeHolder) holder).mJokeStatus.setText(jokeStatus);
            if (jokeImageUrl != null) {
                ((JokeHolder) holder).mImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(jokeImageUrl).into(((JokeHolder) holder).mImageView);
            } else {
                ((JokeHolder) holder).mImageView.setVisibility(View.GONE);
            }
        } else if (holder instanceof CommentHolder) {
            if (jokeImageUrl == null) {
                ((CommentHolder) holder).mTextView.setText(mCommentList.get(position - 1).getCommentBody());
            } else {
                ((CommentHolder) holder).mTextView.setText(mCommentList.get(position - 1).getCommentBody());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.ITEM_TYPE_JOKE.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_COMMENT.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        if (jokeImageUrl == null) {
            return 1 + mCommentList.size();
        } else {
            return 1 + mCommentList.size();
        }
    }


    public static class CommentHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        CommentHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.comment_text);
        }
    }


    public static class JokeHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        TextView mJokeStatus;
        JokeHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.joke_text);
            mImageView = (ImageView) view.findViewById(R.id.joke_image);
            mJokeStatus = (TextView) view.findViewById(R.id.joke_status);
        }
    }

}
