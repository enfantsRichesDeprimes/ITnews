package com.example.itnews;

import android.content.Intent;
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
public class register extends AppCompatActivity  implements View.OnClickListener{
    Button getIdentity;
    Button register;
    EditText zhanghao;
    EditText passWord;
    EditText e_mail;
    EditText identifyingCode;
    EditText passWord2;
    String mail;
    int usage;
    String password;
    String zhangHao;
    String verify;
    String password2;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.register);
        getIdentity=(Button)findViewById(R.id.getIdentity);
        register=(Button)findViewById(R.id.register);
        zhanghao=(EditText)findViewById(R.id.zhanghao);
        passWord=(EditText)findViewById(R.id.passWord);
        e_mail=(EditText)findViewById(R.id.e_mail);
        identifyingCode=(EditText)findViewById(R.id.identifyingCode);
        passWord2=(EditText)findViewById(R.id.passWord2);
        getIdentity.setOnClickListener(this);
        register.setOnClickListener(this);

        mail=e_mail.getText().toString();
        zhangHao=zhanghao.getText().toString();
        password=passWord.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getIdentity:
                mail=e_mail.getText().toString();
                final JSONObject mail_json=new JSONObject();
                try {
                    mail_json.put("email",mail);
                    mail_json.put("usage",1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mail.equals("")){
                    Toast.makeText(com.example.itnews.register.this,"邮箱为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                usage=1;
                 new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,mail_json.toString());
                        RequestBody requestBody = new FormBody.Builder().add("email", mail).add("usage",usage+"").build();
                        Request request=new Request.Builder().url("http://39.106.195.109/itnews/api/reglog/code-reg").post(body).build();
                        Call call=okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(com.example.itnews.register.this,"发送失败",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_json=new JSONObject(res);
                                    String code=res_json.getString("code");
                                    String msg=res_json.getString("msg");
                                    Log.d("132",msg);
                                    if(code.equals("996")){
                                        Looper.prepare();
                                        Toast.makeText(com.example.itnews.register.this,"邮箱不合规范",Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                    else{
                                        Looper.prepare();
                                        Toast.makeText(com.example.itnews.register.this,"发送成功",Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }).start();break;

            case R.id.register:
                verify=identifyingCode.getText().toString();
                mail=e_mail.getText().toString();
                zhangHao=zhanghao.getText().toString();
                password=passWord.getText().toString();
                password2=passWord2.getText().toString();
                if(mail.equals("")||zhangHao.equals("")||password.equals("")||verify.equals("")){
                    Toast.makeText(com.example.itnews.register.this,"输入不能有空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password2)){
                    Toast.makeText(com.example.itnews.register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject register_json=new JSONObject();
                try {
                    register_json.put("username",zhangHao);
                    register_json.put("password",password);
                    register_json.put("email",e_mail);
                    register_json.put("verify",verify);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient1=new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,register_json.toString());
                        RequestBody requestBody1=new FormBody.Builder().add("username",zhangHao).add("password",password).add("email",mail).add("verify",verify).build();
                        Request request1=new Request.Builder().url("http://39.106.195.109/itnews/api/reglog/all-reg").post(body).build();
                        Call call=okHttpClient1.newCall(request1);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(com.example.itnews.register.this,"注册失败",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_result=new JSONObject(res);
                                    String msg=res_result.getString("msg");
                                    Log.d("1234",msg);
                                    if(msg.equals("一切正常")){
                                        Looper.prepare();
                                        Toast.makeText(com.example.itnews.register.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(com.example.itnews.register.this,login.class);
                                        startActivity(intent);
                                        finish();
                                        Looper.loop();
                                    }
                                    else {
                                        Looper.prepare();
                                        Toast.makeText(com.example.itnews.register.this,"输入有误",Toast.LENGTH_SHORT).show();
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
    }
}
