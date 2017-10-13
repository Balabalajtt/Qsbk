package com.example.qsbk.Beans;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/12.
 */

public class JokeDetail {
    private String mJokeText;

    private String mJokeImage;

    private String mJokeStatus;

    private String jokeAuthor;

    public String getJokeAuthor() {
        return jokeAuthor;
    }

    public void setJokeAuthor(String jokeAuthor) {
        this.jokeAuthor = jokeAuthor;
    }

    public String getHeadPhotoUrl() {
        return headPhotoUrl;
    }

    public void setHeadPhotoUrl(String headPhotoUrl) {
        this.headPhotoUrl = headPhotoUrl;
    }

    private String headPhotoUrl;

    public String getJokeStatus() {
        return mJokeStatus;
    }

    public void setJokeStatus(String jokeStatus) {
        mJokeStatus = jokeStatus;
    }

    private List<Comment> mCommentList;

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void setCommentList(List<Comment> commentList) {
        mCommentList = commentList;
    }

    public String getJokeText() {
        return mJokeText;
    }

    public void setJokeText(String jokeText) {
        mJokeText = jokeText;
    }

    public String getJokeImage() {
        return mJokeImage;
    }

    public void setJokeImage(String jokeImage) {
        mJokeImage = jokeImage;
    }

}
