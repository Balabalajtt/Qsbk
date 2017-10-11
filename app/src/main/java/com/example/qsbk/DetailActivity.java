package com.example.qsbk;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;

    private String detailUrl = "";
    private RecyclerView mRecyclerView;
    private List<Comment> mCommentList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private CommentAdapter mCommentAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!mCommentList.isEmpty()) {
                mCommentAdapter.notifyDataSetChanged();
            }
            mCommentAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Joke joke = (Joke) getIntent().getSerializableExtra("joke");
        detailUrl = joke.getDetailUrl();
        mTextView = (TextView) findViewById(R.id.detail_text_view);
        mImageView = (ImageView) findViewById(R.id.detail_image_view);
        mTextView.setText(joke.getJokeText());
        if (joke.getJokeImageUrl() != null) {
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(joke.getJokeImageUrl()).into(mImageView);
        } else {
            mImageView.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mCommentAdapter = new CommentAdapter(mCommentList);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCommentAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);//解决嵌套卡顿

        for (int i = 0; i < 50; i++) {
            Comment c = new Comment();
            c.setCommentBody("************");
            mCommentList.add(c);
        }
        mCommentList.clear();
        requestDate();

        mCommentAdapter.notifyDataSetChanged();

    }

    private void requestDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Connection connection;
                connection = Jsoup.connect(detailUrl);
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");//伪装浏览器获取
                Document document = null;
                try {
                    document = connection.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert document != null;
                final Elements es = document.getElementsByClass("content");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(es.get(0).text());
                    }
                });


                Elements elements = document.getElementsByClass("body");
                for (Element e : elements) {

                    Comment c = new Comment();
                    c.setCommentBody(e.select("span").text());
                    mCommentList.add(c);
                    Log.d("666", "run: " + c.getCommentBody());

                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }



}
