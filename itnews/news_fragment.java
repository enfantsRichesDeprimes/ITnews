package com.example.itnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class news_fragment extends Fragment {
    RecyclerView recyclerView;
    adapter adapter;
    Context context;
    SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);
        adapter = new adapter(context);
        refreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) news_fragment.this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPink));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.i += 5;
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    public class adapter extends RecyclerView.Adapter<com.example.itnews.adapter.LinearViewHolder> {
        Context context;
        int i;
        String[] arr=new String[50];
        List<adapter> list=new ArrayList<>();

        public adapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public com.example.itnews.adapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new com.example.itnews.adapter.LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.new_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final com.example.itnews.adapter.LinearViewHolder holder, final int position) {
            adapter adapter1 =list.get(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://news-at.zhihu.com/api/4/news/latest").build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                JSONArray stories = jsonObject.getJSONArray("stories");
                                for (i = 0; i < stories.length(); i++) {
                                    JSONArray news = stories.getJSONArray(i);
                                    JSONObject news_title = news.getJSONObject(1);
                                    JSONObject news_detailed_url=news.getJSONObject(2);
                                    String detailed_url=news_detailed_url.getString("url");
                                    arr[i]=detailed_url;
                                    String title = news_title.getString("title");
                                    Log.d("123", title);
                                    JSONArray news_ima = news.getJSONArray(5);
                                    JSONObject ima = news_ima.getJSONObject(0);
                                    String ima_news = ima.getString("0");
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
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),news_detailed.class);
                    intent.putExtra(arr[position],"url");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return i;
        }

        public  class LinearViewHolder extends RecyclerView.ViewHolder {
            TextView tv_item;
            ImageView ima;
            public LinearViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
                ima = itemView.findViewById(R.id.ima_item);
            }

        }
    }
}
