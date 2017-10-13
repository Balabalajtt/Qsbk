package com.example.qsbk.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    private int i = 1;

    private List<Joke> mJokeList = new ArrayList<>();
    private List<Joke> mDataList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private JokeAdapter mJokeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    i = 1;
                    mJokeList.clear();
                    for (Joke joke : mDataList) {
                        mJokeList.add(joke);
                    }
                    if (!mJokeList.isEmpty()) {
                        mJokeAdapter.notifyDataSetChanged();
                    }
                    mJokeAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    for (Joke joke : mDataList) {
                        mJokeList.add(joke);
                    }
                    if (!mJokeList.isEmpty()) {
                        mJokeAdapter.notifyDataSetChanged();
                    }
                    mJokeAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mJokeList.isEmpty()) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        //初始recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mJokeAdapter = new JokeAdapter(mJokeList, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mJokeAdapter);
        mJokeAdapter.setOnItemClickListener(new JokeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!mJokeList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("joke", mJokeList.get(position));
                    startActivity(intent);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= mLinearLayoutManager.getItemCount() - 1){
                        Log.d("cdaxca", "onScrollStateChanged: " + "*****" + i);

                        if (i < 13) {
                            i++;
                            requestData(i);
                        } else {
                            Toast.makeText(MainActivity.this, "无更多数据", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        //刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                requestData(1);
            }
        });

        requestData(1);

    }

    public void requestData(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://www.qiushibaike.com/8hr/page/" + i;
                mDataList.clear();
                Connection conn;
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

                    //作者和头像
                    Elements authorInfo = ele.select("div.author");
                    for (Element e : authorInfo) {
                        Elements ee = e.getElementsByTag("img");
                        joke.setHeadPhotoUrl("http:" + ee.get(0).attr("src"));
                        joke.setJokeAuthor(ee.get(0).attr("alt"));
                    }

                    mDataList.add(joke);

                    Log.d("test123", "run: " + joke.getDetailUrl());
                    Log.d("test123", "run: " + joke.getJokeText());
                    Log.d("test123", "run: " + joke.getJokeImageUrl());
                    Log.d("test123", "run: " + joke.getJokeAuthor());
                    Log.d("test123", "run: " + joke.getHeadPhotoUrl());
                    if (mJokeList.size() > 3)
                    Log.d("test123", "run: " + mDataList.size() + mJokeList.get(mJokeList.size() - 1).getJokeText());
                    Log.d("test123", "run: " + "-----------------------------");
                }

                if (i == 1) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

}

