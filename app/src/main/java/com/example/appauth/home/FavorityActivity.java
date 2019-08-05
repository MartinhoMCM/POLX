package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appauth.R;
import com.example.appauth.adapter.MyFavorityPostsAdapter;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FavorityActivity extends AppCompatActivity {

    private User mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Posts mPosts;

    private ArrayList<Favority> favorities;
    private MyFavorityPostsAdapter favorityPostsAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorInternet,mErrorNoResult;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favority);

        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        favorities = new ArrayList<>();
        favorityPostsAdapter = new MyFavorityPostsAdapter(FavorityActivity.this, favorities);

        mRecyclerView = findViewById(R.id.id_favorito_list_user);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.Refresh);
        mCurrentUser = (User)User.getCurrentUser();

        mErrorNoResult = findViewById(R.id.noResults);
        mErrorInternet = findViewById(R.id.noInternet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(FavorityActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(favorityPostsAdapter);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                FavorityActivity.this.loadMyFavority();
            }
        });
    }

    private void loadMyFavority() {

        ParseQuery<Favority> favorityQuery =  Favority.getQuery();
       // favorityQuery.whereEqualTo(Favority.COL_POST, mpost);
        favorityQuery.whereEqualTo(Favority.COL_AUTHOR, mCurrentUser);
        favorityQuery.findInBackground(new FindCallback<Favority>() {
            @Override
            public void done(List<Favority> objecto, ParseException e) {

                if (objecto != null) {

                    if (objecto.size() > 0) {

                        Log.d("comprimento ", ""+objecto.size());

                        mErrorNoResult.setVisibility(View.GONE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        favorities.clear();
                        favorities.addAll(objecto);
                        favorityPostsAdapter.notifyDataSetChanged();

                    } else {

                        mErrorNoResult.setVisibility(View.VISIBLE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);

                        favorities.clear();

                        // There is no Post.
                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                } else {

                    if (e.getCode() == ParseException.CONNECTION_FAILED) {

                        // No internet error
                        mErrorInternet.setVisibility(View.VISIBLE);
                        mErrorNoResult.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);


                    } else {

                        // Something was wrong
                        String error = e.getLocalizedMessage();
                        Toast.makeText(getApplicationContext(),"Encontramos problemas em " +error,Toast.LENGTH_LONG).show();

                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyFavority();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FavorityActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
