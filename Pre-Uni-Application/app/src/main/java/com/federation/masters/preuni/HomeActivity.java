package com.federation.masters.preuni;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.federation.masters.preuni.home.HomePagerAdapter;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity{

    FragmentPagerAdapter homeViewPagerAdapter;

    int currentPage = 0;
    Timer timer;
    Handler handler;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        Button loginAccessButton = findViewById(R.id.loginAccessButton);

        loginAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        ViewPager homeViewPager=(ViewPager)findViewById(R.id.home_viewpager);
        homeViewPagerAdapter=new HomePagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        homeViewPager.setAdapter(homeViewPagerAdapter);
    }

}