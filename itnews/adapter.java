package com.example.itnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class adapter extends RecyclerView.Adapter<adapter.LinearViewHolder> {
    Context context;
    int i;

    public  adapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public adapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.new_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final adapter.LinearViewHolder holder, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client =new OkHttpClient();
                Request request=new Request.Builder().url("https://news-at.zhihu.com/api/4/news/latest").build();
                Call call=client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res=response.body().string();
                        try {
                            JSONObject jsonObject=new JSONObject(res);
                            JSONArray stories=jsonObject.getJSONArray("stories");
                            for(i=0;i<stories.length();i++){
                                JSONArray news=stories.getJSONArray(i);
                                JSONObject news_title=news.getJSONObject(1);
                                String title=news_title.getString("title");
                                Log.d("123",title);
                                JSONArray news_ima=news.getJSONArray(5);
                                JSONObject ima=news_ima.getJSONObject(0);
                                String ima_news=ima.getString("0");
                                holder.tv_item.setText(title);
                                Glide.with(context).load(ima_news).into(holder.ima);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public int getItemCount() {
        return i;
    }

    public static class LinearViewHolder extends RecyclerView.ViewHolder{
        TextView tv_item;
        ImageView ima;
        LinearLayout news_item;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item=itemView.findViewById(R.id.tv_item);
            ima=itemView.findViewById(R.id.ima_item);
news_item=itemView.findViewById(R.id.news_item);
        }
    }
}
