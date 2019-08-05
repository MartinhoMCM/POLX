package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.intro.IntroActivity;
import com.example.appauth.intro.PrefManager;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.example.appauth.proload.SobreActivity;
import com.example.appauth.userprofile.EditarPerfilActivity;
import com.example.appauth.userprofile.UserHomePage_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    HomeAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPagerHome;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce;

    private User mCurrentUser;
    private NavigationView navigationView;
    private ImageView mSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(toolbar.getTitle().length()!=0) {
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        mCurrentUser =(User) ParseUser.getCurrentUser();


        tabLayout =findViewById(R.id.tabLayoutHomeID);
        viewPagerHome=findViewById(R.id.ViewPagerHomeID);
        navigationView =findViewById(R.id.nav_viewHome);
        mSlider = findViewById(R.id.slider);

        mSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrefManager prefManager = new PrefManager(getApplicationContext());
                prefManager.setFirstTimeLaunch(true);

                startActivity(new Intent(HomeActivity.this, IntroActivity.class));
                finish();
            }
        });


        DrawerLayout mDrawerLayout = findViewById(R.id.drawerHome);

        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_viewHome);


        if(navigationView!=null)
        {
            navigationView.setNavigationItemSelectedListener(this);
            if(mCurrentUser !=null){

                HeaderUserDesc();
            }


        }

        //Add Fragment
        adapter = new HomeAdapter(getSupportFragmentManager());
        adapter.AddFragment(new HomePageFragement());
        adapter.AddFragment(new SearchFragment());
        adapter.AddFragment(new MessageHomePage_Fragment());
        adapter.AddFragment(new UserHomePage_Fragment());



        viewPagerHome.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPagerHome);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_pesquisar);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_pretoo);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_usuario);



        TabListener();
    }

    private void TabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tab.getPosition() == 0)
                {
                    toolbar.setTitle("OLX");

                }
                if(tab.getPosition()==1)
                {
                    toolbar.setTitle("Pesquisar");

                }
                if(tab.getPosition()==2)
                {
                    toolbar.setTitle("Mensagens");

                }
                if(tab.getPosition()==3)
                {
                    toolbar.setTitle("Perfil");


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sair_app) {
            startActivity(new Intent(getApplicationContext(), SairOpcoesActivity.class));
            return true;
        }
        if(id == R.id.addPost){

            startActivity(new Intent(getApplicationContext(), PublishActivity.class));
        }

        if (toggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_publicar) {

            startActivity(new Intent(getApplicationContext(), PublishActivity.class));
            return false;

        } else if (id == R.id.id_anuncio) {

            startActivity(new Intent(getApplicationContext(), MyAnuncioActivity.class));
            return false;

        }
        else if(id == R.id.id_pesquisa_geral){

            startActivity(new Intent(getApplicationContext(), SearchAllActivity.class));
            return false;
        }
        else if (id == R.id.favoritos){

            startActivity(new Intent(getApplicationContext(), FavorityActivity.class));
            return false;

        }
        else if (id == R.id.nav_sobre) {

            startActivity(new Intent(getApplicationContext(), SobreActivity.class));
            return false;

        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    public void HeaderUserDesc() {

        CircleImageView mProfilePhoto = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profil_photo);
        TextView headerUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Username);
        LinearLayout UserSetting = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.configUserAccount);

        headerUserName.setText(mCurrentUser.getUserFirstName() +" " + mCurrentUser.getUserLastName());
        if (mCurrentUser.getPhotoUrl().isEmpty()) {
            mProfilePhoto.setImageResource(R.drawable.placeholder);
        } else {

            Glide.with(this)
                    .load(mCurrentUser.getPhotoUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(mProfilePhoto);

        }
        UserSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarPerfilActivity.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser !=null){
            HeaderUserDesc();
        }


    }

    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.login_press_again, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

}
