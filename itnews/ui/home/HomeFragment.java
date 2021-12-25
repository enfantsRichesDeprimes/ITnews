package com.example.itnews.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itnews.NEWS_stored;
import com.example.itnews.R;
import com.example.itnews.news_detailed;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    int i;
    myAdapter adapter;
    List<NEWS_stored> list=new ArrayList<>();
    Intent intent;
    SharedPreferences sharedPreferences;
    String token;
    private HomeViewModel homeViewModel;
    RefreshLayout refreshLayout;
    int count;
    Context context;
    String[] title_list;
    String[] content_list;
    String[] author_list;
    String[] img_author_list;
    String[] img_list;
    String[] img2_list;
    String[] img3_list;

    int i_refresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
       /* final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

       i_refresh=1;
       context=getActivity();

        sharedPreferences=getActivity().getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        Log.d("1321",token);

        title_list=new String[50];
        content_list=new String[50];
        author_list=new String[50];
        img_author_list=new String[50];
        img_list=new String[50];
        img2_list=new String[50];
        img3_list=new String[50];

        refreshLayout=root.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()).setEnableHorizontalDrag(true));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));


        recyclerView=root.findViewById(R.id.recyclerview_fragment_home);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        intent=new Intent(getActivity(), news_detailed.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder().url("http://39.106.195.109/itnews/api/news/recommend/v4?page=1&size=7")
                        .method("GET", null)
                        .addHeader("Authorization",token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)").build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.d("123",res);
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONObject data_json=jsonObject.getJSONObject("data");
                            count=(int) data_json.get("count");
                            Log.d("145",count+"");
                            JSONArray news_json=data_json.getJSONArray("news");
                            for (i = 0; i < news_json.length(); i++){
                                JSONObject news1=news_json.getJSONObject(i);
                                NEWS_stored news_stored=new NEWS_stored();
                                String title1=news1.getString("title");
                                String content=news1.getString("content");
                                JSONObject author_json=news1.getJSONObject("author");
                                String author=author_json.getString("username");
                                String author_img=author_json.getString("avatar");
                                JSONArray news_ima1=news1.getJSONArray("news_pics_set");
                                if(news_ima1.length()!=0){
                                String image1=news_ima1.getString(0);
                                Log.d("198",image1);
                                  /*  String image2=news_ima1.getString(1);
                                    String image3=news_ima1.getString(2);*/
                                news_stored.img_author=author_img;
                    /*            news_stored.image2=image2;
                                news_stored.image3=image3;*/
                                news_stored.author=author;
                                news_stored.content=content;
                                news_stored.title=title1;
                                news_stored.image=image1;
                                list.add(news_stored);}
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter=new myAdapter();
                                    recyclerView.setAdapter(adapter);
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient().newBuilder().build();
                        Request request = new Request.Builder().url("http://39.106.195.109/itnews/api/news/recommend/v4?page=1&size=7")
                                .method("GET", null)
                                .addHeader("Authorization",token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)").build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONObject data_json=jsonObject.getJSONObject("data");
                                    JSONArray news_json=data_json.getJSONArray("news");
                                    for (i = 0; i < news_json.length(); i++){
                                        JSONObject news1=news_json.getJSONObject(i);
                                        NEWS_stored news_stored=new NEWS_stored();
                                        String title1=news1.getString("title");
                                        String content=news1.getString("content");
                                        JSONObject author_json=news1.getJSONObject("author");
                                        String author=author_json.getString("username");
                                        String author_img=author_json.getString("avatar");
                                        JSONArray news_ima1=news1.getJSONArray("news_pics_set");
                                        if(news_ima1.length()!=0){
                                            String image1=news_ima1.getString(0);
                                            news_stored.img_author=author_img;
                                            news_stored.author=author;
                                            news_stored.content=content;
                                            news_stored.title=title1;
                                            news_stored.image=image1;
                                            list.add(news_stored);
                                        }
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter=new myAdapter();
                                            recyclerView.setAdapter(adapter);
                                            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
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
                refreshLayout.finishRefresh(2000);

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                i_refresh++;
                if(i_refresh>count) return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder().build();
                        Request request = new Request.Builder().url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+i_refresh+"&size=7")
                                .method("GET", null)
                                .addHeader("Authorization",token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)").build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res = response.body().string();
                                Log.d("123",res);
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONObject data_json=jsonObject.getJSONObject("data");
                                    JSONArray news_json=data_json.getJSONArray("news");
                                    for (i=0; i < news_json.length(); i++){
                                        JSONObject news1=news_json.getJSONObject(i);
                                        NEWS_stored news_stored=new NEWS_stored();
                                        String title1=news1.getString("title");
                                        String content=news1.getString("content");
                                        JSONObject author_json=news1.getJSONObject("author");
                                        String author=author_json.getString("username");
                                        String author_img=author_json.getString("avatar");
                                        JSONArray news_ima1=news1.getJSONArray("news_pics_set");
                                        if(news_ima1.length()!=0){
                                        String image1=news_ima1.getString(0);
                                        news_stored.content=content;
                                        news_stored.title=title1;
                                        news_stored.image=image1;
                                        news_stored.author=author;
                                        news_stored.img_author=author_img;

                                        list.add(news_stored);}
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("0870","0870");
                                            adapter.notifyDataSetChanged();

                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();

                refreshLayout.finishLoadMore(2000);
            }
        });



        return root;
    }



    class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder>{

        @NonNull
        @Override
        public myAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=View.inflate(getActivity(),R.layout.new_item,null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull myAdapter.MyViewHolder holder, final int position) {
            NEWS_stored news_stored_viewHolder=list.get(position);
            title_list[position]=news_stored_viewHolder.title;
            content_list[position]=news_stored_viewHolder.content;
            author_list[position]=news_stored_viewHolder.author;
            img_list[position]=news_stored_viewHolder.image;
            img2_list[position]=news_stored_viewHolder.image2;
            img3_list[position]=news_stored_viewHolder.image3;
            img_author_list[position]=news_stored_viewHolder.img_author;
            holder.tv_item.setText(news_stored_viewHolder.title);
            Glide.with(getActivity()).load(news_stored_viewHolder.image).into(holder.imageView_item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getActivity(),news_detailed.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("title",title_list[position]);
                    bundle.putString("content",content_list[position]);
                    bundle.putString("author",author_list[position]);
                    bundle.putString("img_author",img_author_list[position]);
                    bundle.putString("img",img_list[position]);
                    bundle.putString("img2",img2_list[position]);
                    bundle.putString("img3",img3_list[position]);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
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
        }
    }
}


    public interface Mylistener{
        void sendValue (String value);
    }

    public Mylistener mylistener;


    public void setMylistener(Mylistener mylistener) {
        this.mylistener = mylistener;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
