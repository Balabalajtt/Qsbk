package com.example.qsbk.Activities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.qsbk.Adapters.MultipleItemAdapter;
import com.example.qsbk.Beans.Comment;
import com.example.qsbk.Beans.Joke;
import com.example.qsbk.Beans.JokeDetail;
import com.example.qsbk.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;

    private JokeDetail mJokeDetail;

    private RecyclerView mRecyclerView;

    private List<Comment> mCommentList = new ArrayList<>();

    private String detailUrl = "";

    private LinearLayoutManager mLinearLayoutManager;

    private MultipleItemAdapter mMultipleItemAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRecyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
            mLinearLayoutManager = new LinearLayoutManager(mContext);
            mMultipleItemAdapter = new MultipleItemAdapter(mContext, mJokeDetail);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mMultipleItemAdapter);
            mMultipleItemAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mContext = this;

        Joke joke = (Joke) getIntent().getSerializableExtra("joke");
        detailUrl = joke.getDetailUrl();

        mJokeDetail = new JokeDetail();
        mJokeDetail.setJokeImage(joke.getJokeImageUrl());
        mJokeDetail.setJokeStatus(joke.getJokeStatus());
        mJokeDetail.setJokeAuthor(joke.getJokeAuthor());
        mJokeDetail.setHeadPhotoUrl(joke.getHeadPhotoUrl());

        requestDate();

//        mRecyclerView.setNestedScrollingEnabled(false);//解决嵌套卡顿

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
                        mJokeDetail.setJokeText(es.get(0).text());
                    }
                });


                Comment comment = new Comment();
                comment.setCommentBody("评论 :");
                mCommentList.add(comment);

                Elements elements = document.getElementsByClass("body");
                for (Element e : elements) {
                    Comment c = new Comment();
                    c.setCommentBody(e.select("span").text());
                    c.setReport(e.parent().parent().getElementsByClass("report").get(0).text());
                    mCommentList.add(c);
                }
                if (mCommentList.size() == 1) {
                    mCommentList.remove(0);
                    comment.setCommentBody("暂无评论");
                    mCommentList.add(comment);
                }

                mJokeDetail.setCommentList(mCommentList);
                Log.d("666", "run: " + mJokeDetail.getCommentList().size());
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

}
