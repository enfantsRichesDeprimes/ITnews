package com.example.itnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class test2 extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter adapter;
    List<NEWS_stored> list=new ArrayList<>();
    int i;
    Intent intent;
    String[] test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_fragment);
        intent=new Intent(test2.this,news_detailed.class);
        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(test2.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder().url("https://news-at.zhihu.com/api/4/news/latest").build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(test2.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.d("1234",res);
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray stories = jsonObject.getJSONArray("stories");
                            for (i = 0; i < stories.length(); i++) {
                                JSONObject news = stories.getJSONObject(i);
                                NEWS_stored news_stored=new NEWS_stored();
                                String detailed_url=news.getString("url");
                                String title = news.getString("title");
                                Log.d("1234",title);
                                Log.d("1234",detailed_url);
                                JSONArray news_ima = news.getJSONArray("images");
                                String image = news_ima.getString(0);
                                Log.d("1234",image);
                                Log.d("1234", "1234");
                                news_stored.url=detailed_url;
                                news_stored.title=title;
                                news_stored.image=image;
                                list.add(news_stored);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter=new myAdapter();
                                    recyclerView.setAdapter(adapter);
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(test2.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();


    }
    class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=View.inflate(test2.this,R.layout.new_item,null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }
        @NonNull
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            NEWS_stored news_stored_viewHolder=list.get(position);
            holder.tv_item.setText(news_stored_viewHolder.title);
            Glide.with(test2.this).load(news_stored_viewHolder.image).into(holder.imageView_item);
            Log.d("321",news_stored_viewHolder.url);
            intent.putExtra(news_stored_viewHolder.url,"url");
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_item;
            ImageView imageView_item;
            public MyViewHolder(@NonNull View itemView){
                super(itemView);
                tv_item=itemView.findViewById(R.id.tv_item);
                imageView_item=itemView.findViewById(R.id.ima_item);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       startActivity(intent);
                    }
                });
            }
        }
    }
}

