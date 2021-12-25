package com.example.itnews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class myarticle_adapter extends RecyclerView.Adapter<myarticle_adapter.LinearViewHolder> {
    Context context;
    int i;

    public myarticle_adapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public myarticle_adapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.mynews_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LinearViewHolder holder, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client =new OkHttpClient();
                Request request=new Request.Builder().url("http://39.106.195.109/itnews/api/self/news-ids").build();
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
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray news=data.getJSONArray("news");
                            JSONObject title=news.getJSONObject(2);
                            String title1=title.getString("title");
                            JSONArray news_ima=news.getJSONArray(1);
                            JSONObject ima=news_ima.getJSONObject(0);
                            String ima_news=ima.getString("0");
                            holder.tv_item.setText(title1);
                            Glide.with(context).load(ima_news).into(holder.ima);

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

    public class LinearViewHolder extends RecyclerView.ViewHolder{
        TextView tv_item;
        ImageView ima;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item=itemView.findViewById(R.id.tv_item);
            ima=itemView.findViewById(R.id.ima_item);

        }
    }
}

