package com.example.qsbk.Beans;

import java.io.Serializable;

/**
 * Created by 江婷婷 on 2017/10/9.
 */

public class Joke implements Serializable {
    private String jokeText;

    private String jokeImageUrl;

    private String detailUrl;

    private String jokeStatus;

    public String getJokeStatus() {
        return jokeStatus;
    }

    public void setJokeStatus(String jokeStatus) {
        this.jokeStatus = jokeStatus;
    }

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

}
