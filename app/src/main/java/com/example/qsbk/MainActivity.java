package com.example.qsbk;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

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

    private Handler handler = new Handler(){
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
                for (int i = 1; i < 5; i++) {
                    Connection conn;
                    String url = yurl + i;
                    conn = Jsoup.connect(url);
                    conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");//伪装浏览器获取
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }

                    //解析加载数据
                    assert doc != null;
                    Elements elements = doc.select("div.article");

                    for (Element element : elements) {
                        Joke joke = new Joke();

                        String text = element.getElementsByTag("span").first().text();
                        String image = element.select("img.illustration").attr("src");

                        joke.setJokeText(text);
                        if (!image.equals("")) {
                            joke.setJokeImageUrl("http:" + image);
                        }
                        if (!TextUtils.isEmpty(text)) {
                            mJokeList.add(joke);
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

}

