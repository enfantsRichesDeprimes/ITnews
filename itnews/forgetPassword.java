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

public class forgetPassword extends AppCompatActivity implements View.OnClickListener{
    EditText email_fgtpsw;
    EditText newPassword;
    EditText verifyingCode;
    Button getVerifyingCode;
    Button submit;

    String email;
    String password;
    String verifyingCode1;
    int usage;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.forgetpassword);
        email_fgtpsw=(EditText)findViewById(R.id.e_mail_fgtpsd);
        newPassword=(EditText)findViewById(R.id.newPassword);
        verifyingCode=(EditText)findViewById(R.id.identifyingCode_fgtpwd);
        getVerifyingCode=(Button)findViewById(R.id.getIdentity_fgtpsw);
        submit=(Button)findViewById(R.id.submit);
        getVerifyingCode.setOnClickListener(this);
        submit.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getIdentity_fgtpsw:
                email=email_fgtpsw.getText().toString();
                password=newPassword.getText().toString();
                verifyingCode1=verifyingCode.getText().toString();
                usage=2;
                if(email.equals("")){
                    Toast.makeText(forgetPassword.this,"邮箱为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject getiden_json=new JSONObject();
                try {
                    getiden_json.put("email",email);
                    getiden_json.put("usage",2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {Log.d("123","123");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,getiden_json.toString());
                        RequestBody requestBody = new FormBody.Builder().add("email", email).add("usage",usage+"").build();
                        Request request=new Request.Builder().url("http://39.106.195.109/itnews/api/reglog/code-reg").post(body).build();
                        Call call=okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(forgetPassword.this,"发送失败",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_json=new JSONObject(res);
                                    String code=res_json.getString("code");
                                    String msg=res_json.getString("msg");
                                    Log.d("132",code);
                                    if(msg.equals("一切正常")){
                                        Looper.prepare();
                                        Toast.makeText(forgetPassword.this,"发送成功",Toast.LENGTH_SHORT).show();
                                        Looper.loop();

                                    }
                                    else{Looper.prepare();
                                        Toast.makeText(forgetPassword.this,"邮箱不合规范",Toast.LENGTH_SHORT).show();
                                        Looper.loop();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }).start();break;

            case R.id.submit:
                email=email_fgtpsw.getText().toString();
                password=newPassword.getText().toString();
                verifyingCode1=verifyingCode.getText().toString();
                usage=2;
                if(email.equals("")||verifyingCode1.equals("")||password.equals("")){
                    Toast.makeText(forgetPassword.this,"输入不能有空",Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject submit_json=new JSONObject();
                try {
                    submit_json.put("email",email);
                    submit_json.put("verify",verifyingCode1);
                    submit_json.put("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient=new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,submit_json.toString());
                        RequestBody requestBody=new FormBody.Builder().add("email",email).add("verify",verifyingCode1).add("password",password).build();
                        Request request=new Request.Builder().url("http://39.106.195.109/itnews/api/reglog/pwd-recall").post(body).build();
                        Call call=okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(forgetPassword.this,"检查网络",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_result=new JSONObject(res);
                                    String msg=res_result.getString("msg");
                                    Log.d("123",msg);
                                    if(msg.equals("一切正常")){
                                        Looper.prepare();
                                        Toast.makeText(forgetPassword.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(forgetPassword.this,login.class);
                                        startActivity(intent);
                                        finish();
                                        Looper.loop();
                                    }
                                    else {
                                        Looper.prepare();
                                        Toast.makeText(forgetPassword.this,"输入有误",Toast.LENGTH_SHORT).show();
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
