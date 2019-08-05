package com.example.appauth.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appauth.R;
import com.example.appauth.adapter.NotificationsAdapter;
import com.example.appauth.home.HomeActivity;
import com.example.appauth.models.Notifications;
import com.example.appauth.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private User mCurrentUser;
    private SwipeRefreshLayout swipeRefreshLayout;


    private ArrayList<Notifications> mNotify;
    private NotificationsAdapter mNotificationsAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorInternet,mErrorNoResult;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        mNotify = new ArrayList<>();
        mNotificationsAdapter = new NotificationsAdapter(NotificationsActivity.this, mNotify);
        mRecyclerView = findViewById(R.id.notifyUser);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout =findViewById(R.id.Refresh);
        mCurrentUser = (User)User.getCurrentUser();

        mErrorNoResult = findViewById(R.id.noResults);
        mErrorInternet = findViewById(R.id.noInternet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNotificationsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

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
                NotificationsActivity.this.loadNotify();
            }
        });
    }

    private void loadNotify() {

        ParseQuery<Notifications> query = Notifications.getQuery();

        query.whereEqualTo(Notifications.COL_TO_AUTHOR, mCurrentUser);
        query.whereNotEqualTo(Notifications.COL_AUTHOR, mCurrentUser);

        query.include(Notifications.COL_TO_AUTHOR);
        query.orderByDescending(Notifications.COL_CREATED_AT);

        query.findInBackground(new FindCallback<Notifications>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<Notifications> objecto, ParseException e) {

                if (objecto != null) {

                    if (objecto.size() > 0) {
                        Log.d("comprimento ", ""+objecto.size());

                        mErrorNoResult.setVisibility(View.GONE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        mNotify.clear();
                        mNotify.addAll(objecto);
                        mNotificationsAdapter.notifyDataSetChanged();

                    } else {
                        mErrorNoResult.setVisibility(View.VISIBLE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);

                        mNotify.clear();

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
    protected void onResume() {
        super.onResume();
        loadNotify();
    }
}
