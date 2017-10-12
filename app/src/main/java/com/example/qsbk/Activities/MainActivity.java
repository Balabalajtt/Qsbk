package com.example.qsbk.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qsbk.Beans.Joke;
import com.example.qsbk.Adapters.JokeAdapter;
import com.example.qsbk.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<Joke> mJokeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private JokeAdapter mJokeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mJokeStatus;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!mJokeList.isEmpty()) {
                mJokeAdapter.notifyDataSetChanged();
            }
            mJokeAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mJokeAdapter = new JokeAdapter(mJokeList, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mJokeAdapter);
        mJokeAdapter.setOnItemClickListener(new JokeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("joke", mJokeList.get(position));
                startActivity(intent);
            }
        });

        mJokeStatus = (TextView) findViewById(R.id.comment_text);
        //刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        requestData();

    }

    public void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String yurl = "https://www.qiushibaike.com/8hr/page/";
                mJokeList.clear();
                for (int i = 1; i < 2; i++) {

                    Connection conn;
                    String url = yurl + i;
                    conn = Jsoup.connect(url);
                    //伪装浏览器获取
                    conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //解析加载数据
                    assert doc != null;
                    Elements elements = doc.getElementsByClass("article");
                    for (Element ele : elements) {
                        Joke joke = new Joke();

                        //详细页网址
                        Elements deu = ele.getElementsByTag("a");
                        for (Element e : deu) {
                            if (e.attr("href").length() > 15 && e.attr("href").substring(0, 3).equals("/ar")) {
                                joke.setDetailUrl("https://www.qiushibaike.com" + e.attr("href"));
                                break;
                            }
                        }

                        //段子内容
                        Elements eee = ele.getElementsByClass("content");
                        String text = eee.get(0).getElementsByTag("span").first().text();
                        joke.setJokeText(text);

                        //配图
                        String image = ele.select("img.illustration").attr("src");
                        if (!image.equals("")) {
                            joke.setJokeImageUrl("http:" + image);
                        }

                        //好笑评论数
                        eee = ele.getElementsByTag("i");
                        String jokeStatus = eee.get(0).text() + "好笑 ~ " + eee.get(1).text() + "评论";
                        Log.d("232323", "run: " + jokeStatus);
                        joke.setJokeStatus(jokeStatus);


                        mJokeList.add(joke);

                        Log.d("test123", "run: " + joke.getDetailUrl());
                        Log.d("test123", "run: " + joke.getJokeText());
                        Log.d("test123", "run: " + joke.getJokeImageUrl());
                        Log.d("test123", "run: " + "-----------------------------");

                    }

                }

                handler.sendEmptyMessage(0);
            }
        }).start();
    }

}

