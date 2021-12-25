package com.example.itnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.itnews.ui.dashboard.DashboardFragment;
import com.example.itnews.ui.home.HomeFragment;
import com.example.itnews.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity_bottomnavigation extends AppCompatActivity {
    int lastFragment;
    Fragment[] fragments;
    HomeFragment fragment0;
    DashboardFragment fragment1;
    NotificationsFragment fragment2;
    SharedPreferences sharedPreferences;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottomnavigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        sharedPreferences=getSharedPreferences("token",0);
        token=sharedPreferences.getString("token","");
        Log.d("123",token);
       if(token.equals("")){
            Intent intent=new Intent(MainActivity_bottomnavigation.this,login.class);
            startActivity(intent);
       }
        fragment0=new HomeFragment();
        fragment1=new DashboardFragment();
        fragment2=new NotificationsFragment();
        fragments=new Fragment[]{fragment0,fragment1,fragment2};
        FragmentTransaction beginTransaction=getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content_bottomNavigation,fragment0).add(R.id.content_bottomNavigation,fragment1).add(R.id.content_bottomNavigation,fragment2);
        beginTransaction.hide(fragment0).hide(fragment1).hide(fragment2);
        beginTransaction.addToBackStack(null);
        beginTransaction.hide(fragment1).hide(fragment2);
        beginTransaction.show(fragment0);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
        lastFragment=0;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction beginTransaction=getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.navigation_news:{
                        beginTransaction.hide(fragment1).hide(fragment2);
                        beginTransaction.show(fragment0);
                        beginTransaction.addToBackStack(null);
                        beginTransaction.commit();
                        break;
                    }
                    case R.id.navigation_myNews:{
                        beginTransaction.hide(fragment0).hide(fragment2);
                        beginTransaction.show(fragment1);
                        beginTransaction.addToBackStack(null);
                        beginTransaction.commit();
                        break;
                    }
                    case R.id.navigation_personal:{
                        beginTransaction.hide(fragment0).hide(fragment1);
                        beginTransaction.show(fragment2);
                        beginTransaction.addToBackStack(null);
                        beginTransaction.commit();
                        break;
                    }
                }
                return false;
            }
        });
    }

}
