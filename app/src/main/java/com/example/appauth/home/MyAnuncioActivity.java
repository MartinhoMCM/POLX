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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appauth.R;
import com.example.appauth.adapter.MyUserPostsAdapter;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MyAnuncioActivity extends AppCompatActivity {

    private User mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;


    private ArrayList<Posts> mPosts;
    private MyUserPostsAdapter mPostsAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorInternet,mErrorNoResult;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meus_anuncio_fragment);

        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        mPosts = new ArrayList<>();
        mPostsAdapter = new MyUserPostsAdapter(MyAnuncioActivity.this, mPosts);
        mRecyclerView = findViewById(R.id.listaUser);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout =findViewById(R.id.Refresh);
        mCurrentUser = (User)User.getCurrentUser();

        mErrorNoResult = findViewById(R.id.noResults);
        mErrorInternet = findViewById(R.id.noInternet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyAnuncioActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPostsAdapter);

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
                MyAnuncioActivity.this.loadPosts();
            }
        });

    }


    private void loadPosts() {

        ParseQuery<Posts> queryPosts = Posts.getQuery();
        queryPosts.whereEqualTo(Posts.COL_AUTHOR, mCurrentUser);
        queryPosts.whereExists(Posts.COL_IMAGE);
        queryPosts.include(Posts.COL_AUTHOR);
        queryPosts.orderByDescending(Posts.COL_CREATED_AT);

        queryPosts.findInBackground(new FindCallback<Posts>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<Posts> objecto, ParseException e) {

                if (objecto != null) {

                    if (objecto.size() > 0) {
                        Log.d("comprimento ", ""+objecto.size());

                        mErrorNoResult.setVisibility(View.GONE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        mPosts.clear();
                        mPosts.addAll(objecto);
                        mPostsAdapter.notifyDataSetChanged();

                    } else {
                        mErrorNoResult.setVisibility(View.VISIBLE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);

                        mPosts.clear();

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

                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
         loadPosts();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyAnuncioActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
