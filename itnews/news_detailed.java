package com.example.itnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.itnews.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class news_detailed extends AppCompatActivity  {
    String content;
    String title;
    String author_name;
    String img_author;
    String img;
    String img2;
    String img3;

    Button like;
    Button collection;
    Button comment;
    SharedPreferences sharedPreferences;
    String token;
    List<NEWS_stored> list=new ArrayList<>();
    int i;
    TextView tv_title;
    TextView tv_content;
    TextView tv_author_name;
    ImageView author_img;
    ImageView img_news_detailed;
    ImageView img2_news_detailed;
    ImageView img3_news_detailed;

    Context context;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.news_detailed);
        context=this;
        sharedPreferences=getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        like=(Button)findViewById(R.id.like_news_detailed);
        collection=(Button)findViewById(R.id.collection_news_detailed);
        comment=(Button)findViewById(R.id.comment_news_detailed);

        HomeFragment homeFragment=(HomeFragment)getSupportFragmentManager().findFragmentById(R.id.home_fragment);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        title=bundle.getString("title");
        tv_title=(TextView)findViewById(R.id.title_news_detailed);
        tv_title.setText(title);

       content=bundle.getString("content");
        tv_content=(TextView)findViewById(R.id.content_news_detailed);
        tv_content.setText(content);

        author_name=bundle.getString("author");
        tv_author_name=(TextView)findViewById(R.id.author_name_news_detailed);
        tv_author_name.setText(author_name);

       img_author=bundle.getString("img_author");
        author_img=(ImageView)findViewById(R.id.author_headShot_news_detailed);
        Glide.with(news_detailed.this).load(img_author).into(author_img);

        img=bundle.getString("img");
        img_news_detailed=(ImageView)findViewById(R.id.img_news_detailed);
        Glide.with(news_detailed.this).load(img).into(img_news_detailed);

       /* img2=bundle.getString("img2");
        img2_news_detailed=(ImageView)findViewById(R.id.img2_news_detailed);
        Glide.with(news_detailed.this).load(img).into(img2_news_detailed);

        img3=bundle.getString("img3");
        img3_news_detailed=(ImageView)findViewById(R.id.img3_news_detailed);
        Glide.with(news_detailed.this).load(img).into(img3_news_detailed);*/

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/operator/1/like")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String res=response.body().string();
                            JSONObject res_json=new JSONObject(res);
                            String msg=res_json.getString("msg");
                            Log.d("4321",msg);
                            JSONObject data_json=res_json.getJSONObject("data");
                            String isLike1=data_json.getString("isLike");
                            if(!isLike1.equals("0")){
                                Looper.prepare();
                                Toast.makeText(news_detailed.this, "已点赞", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            else {
                                Looper.prepare();
                                Toast.makeText(news_detailed.this, "已取消点赞", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/operator/1/star")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String res=response.body().string();
                            JSONObject res_json=new JSONObject(res);
                            JSONObject data=res_json.getJSONObject("data");
                            String isStar=data.getString("isStar");
                            if(isStar.equals("1")){
                                Looper.prepare();
                                Toast.makeText(news_detailed.this, "已收藏", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            else if(isStar.equals("0")){
                                Looper.prepare();
                                Toast.makeText(news_detailed.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"content\": \"exercitation\"\n}");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/operator//comment")
                                .method("POST", body)
                                .addHeader("Authorization", "")
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String res=response.body().string();
                            JSONObject res_json=new JSONObject(res);
                            JSONArray data=res_json.getJSONArray("data");
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }}


