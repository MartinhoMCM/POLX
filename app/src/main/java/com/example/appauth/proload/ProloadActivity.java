package com.example.appauth.proload;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.intro.IntroActivity;
import com.example.appauth.models.User;
import com.parse.ParseUser;

public class ProloadActivity extends AppCompatActivity {

    User CurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proload);
        CurrentUser = (User) ParseUser.getCurrentUser();


        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    if (CurrentUser != null) {
                        Intent i = new Intent(ProloadActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ProloadActivity.this, IntroActivity.class);
                        startActivity(i);
                    }

                    finish();
                }
            }
        };
        thread.start();
       }
}

