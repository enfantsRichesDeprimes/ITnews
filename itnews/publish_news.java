package com.example.itnews;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class publish_news extends AppCompatActivity {
    EditText title;
    EditText content;
    Button sure_publis_news;
    SharedPreferences sharedPreferences;
    String token;
    String tag;
    String img_ids;
    ImageView addImage;
    PopupWindow pop;
    int TAKE_PHOTO=1;
    Uri imageUri;
    int CHOOSE_PHOTO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_news);

        sharedPreferences=getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        addImage=(ImageView) findViewById(R.id.addImage_publish_news);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        title=(EditText)findViewById(R.id.title_publish_news);
        content=(EditText)findViewById(R.id.content_publish_news);
        tag="2";
        img_ids="5,6,7";
        sure_publis_news=(Button)findViewById(R.id.sure_publish_news);
        sure_publis_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title_str=title.getText().toString();
                final String content_str=content.getText().toString();
                if(title_str.equals("")||content_str.equals("")){
                    Toast.makeText(publish_news.this,"输入有空",Toast.LENGTH_SHORT).show();
                }
                    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "title="+title_str+"&content="+content_str+"&tag=1&img_ids=6");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/release")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String res=response.body().string();
                            Log.d("1235",res);
                            JSONObject res_json=new JSONObject(res);
                            String msg=res_json.getString("msg");
                            Log.d("1235",msg);
                            Looper.prepare();
                            Toast.makeText(publish_news.this,msg,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(publish_news.this,MainActivity_bottomnavigation.class);
                            startActivity(intent);
                            Looper.loop();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                            }
                        }).start();
                    }
            });
        }




    private void showPop(){
        View bottomView=View.inflate(publish_news.this,R.layout.layout_bottom_dialog,null);
        TextView mAlbum=bottomView.findViewById(R.id.tv_album);
        TextView mCamera=bottomView.findViewById(R.id.tv_camera);
        TextView mCancel=bottomView.findViewById(R.id.tv_cancel);
        pop=new PopupWindow(bottomView,-1,-2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp=publish_news.this.getWindow().getAttributes();
        lp.alpha=0.5f;
        publish_news.this.getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=publish_news.this.getWindow().getAttributes();
                lp.alpha=1f;
                publish_news.this.getWindow().setAttributes(lp);
            }
        });
        /*pop.setAnimationStyle(R.style.);*/
        pop.showAtLocation(publish_news.this.getWindow().getDecorView(), Gravity.BOTTOM,0,0);

        View.OnClickListener clickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_album:
                        if(ContextCompat.checkSelfPermission(publish_news.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(publish_news.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            openAlbum();
                        }break;
                    case R.id.tv_camera:
                        File outputImage=new File(publish_news.this.getExternalCacheDir(),"output_image.jpg");
                        try {
                            if(outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT>=24){
                            imageUri= FileProvider.getUriForFile(publish_news.this,"com.example.cameraalbumtest.fileprovider",outputImage);
                        }else {
                            imageUri= Uri.fromFile(outputImage);
                        }
                        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                        break;
                    case R.id.tv_cancel:
                        closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };
        mCamera.setOnClickListener(clickListener);
        mAlbum.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }
    public void closePopupWindow(){
        if(pop!=null&&pop.isShowing()){
            pop.dismiss();
            pop=null;
        }
    }
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(publish_news.this.getContentResolver().openInputStream(imageUri));
                        addImage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }break;
            case 2:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKiKai(data);
                    }else {
                        handleImageBeforeKiKai(data);
                    }
                }break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKiKai(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(publish_news.this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri, null);
            }
            displayImage(imagePath);
        }
    }
    private void handleImageBeforeKiKai(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection){
        String path=null;
        Cursor cursor=publish_news.this.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String path) {
        if(path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            addImage.setImageBitmap(bitmap);
        }else {
            Toast.makeText(publish_news.this,"检查网络",Toast.LENGTH_SHORT).show();
        }
    }
}