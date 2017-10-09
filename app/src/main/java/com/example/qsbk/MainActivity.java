package com.example.qsbk;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

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
    JokeAdapter mJokeAdapter;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mJokeAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mJokeAdapter = new JokeAdapter(mJokeList, this);
        mRecyclerView.setAdapter(mJokeAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String yurl = "https://www.qiushibaike.com/8hr/page/";
                Connection conn;
                for (int i = 1; i < 2; i++) {
                    String url = yurl + i;
                    conn = Jsoup.connect(url);
                    conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements elements = doc.select("div.article");
                    mJokeList.clear();
                    for (Element element : elements) {
                        String joke = element.getElementsByTag("span").first().text();
                        String img = element.select("img.illustration").attr("src");
                        Log.d("lala", "run: " + img + mJokeList.size());

                        Joke j = new Joke();
                        j.setJokeText(joke);
                        if (!img.equals("")) {
                            j.setJokeImageUrl("http:" + img);
                        }
                        if (!TextUtils.isEmpty(joke)) {
                            mJokeList.add(j);
                        }
                        Log.d("123", "run: " + j.getJokeImageUrl());
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!mJokeList.isEmpty()) {
                            mJokeAdapter.notifyDataSetChanged();
                        }
                    }
                });
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

}

