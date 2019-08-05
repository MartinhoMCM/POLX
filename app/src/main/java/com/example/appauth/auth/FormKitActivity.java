package com.example.appauth.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appauth.R;
import com.example.appauth.home.HomeActivity;

public class FormKitActivity extends AppCompatActivity implements OnClickListener {

    private LinearLayout mMenuFormTab;
    private ImageView mIntroBack,mCloud1;
    private Animation mCloudAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formkit);
        // Set up the login form.

        mMenuFormTab = (LinearLayout) findViewById(R.id.menu_form_tab);
        mIntroBack = (ImageView) findViewById(R.id.introBack);
        mCloud1 = (ImageView) findViewById(R.id.cloud1);
        mCloudAnimation = AnimationUtils.loadAnimation(this,R.anim.item_aimacao_direita);
        mCloud1.setAnimation(mCloudAnimation);

        mIntroBack.setOnClickListener(this);
        mMenuFormTab.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (mMenuFormTab == v) {
            startActivity(new Intent(getApplicationContext(),
                    TabFormActivity.class));
        }
        else if (mIntroBack == v){
            startActivity(new Intent(getApplicationContext(),
                    HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

}

