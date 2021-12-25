package com.example.itnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login extends AppCompatActivity implements View.OnClickListener {
    EditText username;
    EditText password;
    Button forgetPassword;
    Button register;
    Button login;
    String username1;
    String password1;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password_login);
        forgetPassword = (Button) findViewById(R.id.forgetPassword);
        register = (Button) findViewById(R.id.register_login);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_login:
                Intent intent = new Intent(com.example.itnews.login.this, com.example.itnews.register.class);
                startActivity(intent);
                break;

            case R.id.forgetPassword:
                Intent intent1 = new Intent(com.example.itnews.login.this, com.example.itnews.forgetPassword.class);
                startActivity(intent1);
                break;

            case R.id.login:
                username1 = username.getText().toString();
                password1 = password.getText().toString();
                if (username1.equals("") || password1.equals("")) {
                    Toast.makeText(com.example.itnews.login.this, "输入有空", Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject login_json=new JSONObject();
                try {
                    login_json.put("username",username1);
                    login_json.put("password",password1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,login_json.toString());
                        RequestBody requestBody = new FormBody.Builder().add("username", username1).add("password", password1).build();
                        Request request = new Request.Builder().url("http://39.106.195.109/itnews/api/reglog/all-log").post(body).build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(login.this, "登录失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_json=new JSONObject(res);
                                    String msg=res_json.getString("msg");
                                    Log.d("132",msg);
                                    if(msg.equals("一切正常")){
                                        JSONObject data=res_json.getJSONObject("data");
                                        String token123=(String) data.get("token");
                                        Log.d("132",token123);
                                        SharedPreferences sharedPreferences=getSharedPreferences("token",0);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("token",token123);
                                        editor.apply();
                                        Looper.prepare();
                                        Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(login.this,MainActivity_bottomnavigation.class);Log.d("1234","1234");
                                        startActivity(intent);
                                        finish();
                                        Looper.loop();
                                    }
                                    else{
                                        Looper.prepare();
                                        Toast.makeText(login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
        }
    }}
