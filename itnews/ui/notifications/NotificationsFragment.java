package com.example.itnews.ui.notifications;

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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.itnews.R;
import com.example.itnews.changeinformation_personal_fragment;
import com.example.itnews.forgetPassword;
import com.example.itnews.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {
    TextView username;
    TextView resume;
    ImageView headShot;
    TextView gender;
    TextView star_num;
    private Button changePassword;
    Button re_login;
    int TAKE_PHOTO=1;
    Uri imageUri;
    int CHOOSE_PHOTO=2;
    PopupWindow pop;
    int i;
    SharedPreferences sharedPreferences;
    String token;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        sharedPreferences=getActivity().getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        Log.d("132",token);

        username=root.findViewById(R.id.username_personal_fragment);
        resume=root.findViewById(R.id.resume_personal_fragment);
        headShot=root.findViewById(R.id.headshot);
        gender=root.findViewById(R.id.gender);
        star_num=root.findViewById(R.id.star_num);
        changePassword=root.findViewById(R.id.changePassword_personal_fragment);
        re_login=root.findViewById(R.id.re_login);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/info")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String res=response.body().string();
                    Log.d("0769",res);
                    JSONObject res_json=new JSONObject(res);
                    JSONObject data=res_json.getJSONObject("data");
                    final String nickname=data.getString("nickname");
                    final String info=data.getString("info");
                    final String gender_str=data.getString("gender");
                    final String star_num_str=data.getString("star_num");
                    final String avatar=data.getString("avatar");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.append(nickname);
                            resume.append(info);
                            gender.append(gender_str);
                            star_num.append(star_num_str);
                            Glide.with(getActivity()).load(avatar).into(headShot);
                        }
                    });
                }


                catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), forgetPassword.class);
                startActivity(intent);
            }
        });

        re_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), changeinformation_personal_fragment.class);
                startActivity(intent);
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), changeinformation_personal_fragment.class);
                startActivity(intent);
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),changeinformation_personal_fragment.class);
                startActivity(intent);
            }
        });


    }



    private void showPop(){
        View bottomView=View.inflate(getActivity(),R.layout.layout_bottom_dialog,null);
        TextView mAlbum=bottomView.findViewById(R.id.tv_album);
        TextView mCamera=bottomView.findViewById(R.id.tv_camera);
        TextView mCancel=bottomView.findViewById(R.id.tv_cancel);
        pop=new PopupWindow(bottomView,-1,-2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha=0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        /*pop.setAnimationStyle(R.style.);*/
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM,0,0);

        View.OnClickListener clickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_album:
                        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            openAlbum();
                        }break;
                    case R.id.tv_camera:
                        File outputImage=new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                        try {
                            if(outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT>=24){
                            imageUri= FileProvider.getUriForFile(getActivity(),"com.example.cameraalbumtest.fileprovider",outputImage);
                        }else {
                            imageUri=Uri.fromFile(outputImage);
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
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        headShot.setImageBitmap(bitmap);
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
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
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
        Cursor cursor=getActivity().getContentResolver().query(uri,null,selection,null,null);
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
            headShot.setImageBitmap(bitmap);
        }else {
            Toast.makeText(getActivity(),"检查网络",Toast.LENGTH_SHORT).show();
        }
    }

}
