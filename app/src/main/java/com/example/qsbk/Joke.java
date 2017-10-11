package com.example.qsbk;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 江婷婷 on 2017/10/9.
 */

public class Joke implements Serializable {
    private String jokeText;

    private String jokeImageUrl;

    private String detailUrl;

    private List<Comment> commentList;

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }

    public String getJokeImageUrl() {
        return jokeImageUrl;
    }

    public void setJokeImageUrl(String jokeImageUrl) {
        this.jokeImageUrl = jokeImageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

}
