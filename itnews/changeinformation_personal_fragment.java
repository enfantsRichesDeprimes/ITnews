package com.example.itnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
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

public class changeinformation_personal_fragment extends AppCompatActivity {
    EditText username;
    EditText resume;
    TextView gender;
    Button sure;
    int gender_int;
    SharedPreferences sharedPreferences;
    String token;
    PopupWindow pop;
    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeinformation_personal_fragment);
        sharedPreferences=getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        Log.d("1322",token);
        gender_int=-1;
        username=(EditText)findViewById(R.id.changeNickname);
        resume=(EditText)findViewById(R.id.changeResume);
        gender=(TextView)findViewById(R.id.gender_changeInformation);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop1();

            }
        });
        arrow=(ImageView)findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop1();
            }
        });
        sure=(Button)findViewById(R.id.sure_changeInformation_personal_fragment);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username1=username.getText().toString();
                final String resume1=resume.getText().toString();
                String gender1=gender.getText().toString();
                if(username1.equals("")||resume1.equals("")) {
                    Toast.makeText(changeinformation_personal_fragment.this, "输入有空", Toast.LENGTH_SHORT).show();
                }
                if(gender_int==-1) {
                    Toast.makeText(changeinformation_personal_fragment.this,"性别不符合规范",Toast.LENGTH_SHORT).show();
                }
                final JSONObject sure_json=new JSONObject();
                try {
                    sure_json.put("nickname",username1);
                    sure_json.put("info",resume1);
                    sure_json.put("gender",gender_int);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        MediaType mediaType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(mediaType,sure_json.toString());
                        RequestBody requestBody = new FormBody.Builder().add("username", username1).add("info",resume1).build();
                        Request request = new Request.Builder().url("http://39.106.195.109/itnews/api/self/info-refresh").post(body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json").build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(changeinformation_personal_fragment.this, "修改失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject res_json=new JSONObject(res);
                                    String msg=res_json.getString("msg");
                                    Log.d("132",msg);
                                    if(msg.equals("个人信息更改成功")){
                                        Looper.prepare();
                                        Toast.makeText(changeinformation_personal_fragment.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(changeinformation_personal_fragment.this,MainActivity_bottomnavigation.class);
                                        startActivity(intent);
                                        finish();
                                        Looper.loop();
                                    }
                                    else{
                                        Looper.prepare();
                                        Toast.makeText(changeinformation_personal_fragment.this, "修改失败", Toast.LENGTH_SHORT).show();
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
        });
}

private void showPop1(){
        View bottomView=View.inflate(changeinformation_personal_fragment.this,R.layout.gender_choice,null);
     TextView male=bottomView.findViewById(R.id.tv_male);
     TextView female=bottomView.findViewById(R.id.tv_female);
     TextView cancel=bottomView.findViewById(R.id.tv_cancel);
    pop=new PopupWindow(bottomView,-1,-2);
    pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pop.setOutsideTouchable(true);
    pop.setFocusable(true);
    WindowManager.LayoutParams lp=changeinformation_personal_fragment.this.getWindow().getAttributes();
    lp.alpha=0.5f;
    changeinformation_personal_fragment.this.getWindow().setAttributes(lp);
    pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp=changeinformation_personal_fragment.this.getWindow().getAttributes();
            lp.alpha=1f;
            changeinformation_personal_fragment.this.getWindow().setAttributes(lp);
        }
    });
    pop.showAtLocation(changeinformation_personal_fragment.this.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_male:
                    gender_int = 1;
                    break;
                case R.id.tv_female:
                    gender_int = 0;
                    break;
                case R.id.tv_cancel:
                    closePopupWindow();
                    break;
            }
            if(gender_int==1){
                gender.setText("男");
            }
            else if(gender_int==0){
                gender.setText("女");
            }
            closePopupWindow();
        }
    };
    male.setOnClickListener(clickListener);
    female.setOnClickListener(clickListener);
    cancel.setOnClickListener(clickListener);
    }

    private void closePopupWindow() {
        if(pop!=null&&pop.isShowing()){
            pop.dismiss();
            pop=null;
        }
    }

}

