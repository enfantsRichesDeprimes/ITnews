package com.example.itnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {
    String token;
    SharedPreferences sharedPreferences;
    myArticle_fragment myArticle_fragment;
    personal_fragment personal_fragment;
    private int lastFragment;
    Fragment[] fragments;

    ViewPager viewPager;
    MenuItem menuItem;
    BottomNavigationView bottomNavigationView;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("toke",0);
        token=sharedPreferences.getString("token","");
        /*if(token.equals("")){
            Intent intent=new Intent(MainActivity.this,login.class);
            startActivity(intent);
        }*/
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        fragments=new Fragment[]{myArticle_fragment,personal_fragment};
        myArticle_fragment=new myArticle_fragment();
        lastFragment=0;
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news_menu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.myArticle_menu:
                        viewPager.setCurrentItem(1);
                    case R.id.person_menu:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(menuItem!=null){
                    menuItem.setChecked(false);
                }else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem=bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }




}

