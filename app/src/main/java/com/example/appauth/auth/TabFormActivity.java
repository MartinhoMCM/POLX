package com.example.appauth.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.appauth.R;

import com.example.appauth.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TabFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_form);

        ViewPager mViewPager = findViewById(R.id.viewPagerform);
        TabLayout mTabLayout = findViewById(R.id.main_tabs_form);

        mViewPager.setAdapter(setViewPager());
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private PagerAdapter setViewPager() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragmentos(new LoginFragment(), "INICIAR SESSÃO");
        viewPagerAdapter.addFragmentos(new RegisterFragment(), "NOVA CONTA");

        return viewPagerAdapter;
    }
}
