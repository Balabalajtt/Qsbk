package com.example.qsbk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/11.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> mCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView commentText;

        public ViewHolder(View itemView) {
            super(itemView);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);
        }
    }

    public CommentAdapter(List<Comment> commentList) {
        mCommentList = commentList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position) {
        Comment comment = mCommentList.get(position);
        holder.commentText.setText(comment.getCommentBody());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}
