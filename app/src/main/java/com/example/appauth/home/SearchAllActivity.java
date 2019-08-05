package com.example.appauth.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appauth.R;
import com.example.appauth.adapter.SearchAdapter;
import com.example.appauth.models.Posts;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchAllActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Posts> mPosts;
    private SearchAdapter mSearchAdapter;
    private RecyclerView mRecyclerView;
    SearchView searchView;
    private LinearLayout mErrorInternet,mErrorNoResult;
    private TextView mTextNotFoud,mPesquisaCount;
    private ProgressBar mProgressBarUi;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);

        mPosts = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(this, mPosts);
        searchView = (SearchView) findViewById(R.id.psqGeral);
        mRecyclerView = findViewById(R.id.listaUp);
        mErrorNoResult = findViewById(R.id.noResults);
        mTextNotFoud = findViewById(R.id.id_text_field);
        mPesquisaCount = findViewById(R.id.pesquisaCount);
        mErrorInternet = findViewById(R.id.noInternet);
        mProgressBarUi = findViewById(R.id.progress_bar_id);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
       // mSearchAdapter.onItemClickListener(SearchAllActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSearchAdapter);


        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Pesquisa no OLX ... ");


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        CharSequence text = searchView.getQuery();

        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        ParseQuery<Posts> query1 = Posts.getQuery();
        query1.whereMatches(Posts.COL_TITLE_LOWER_CASE, newText.toLowerCase());

        if (newText.isEmpty()){
            mProgressBarUi.setVisibility(View.GONE);
        }else {
            mProgressBarUi.setVisibility(View.VISIBLE);
        }
        query1.findInBackground(new FindCallback<Posts>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<Posts> objects, ParseException e) {

                if (objects != null){
                    if (objects.size() > 0) {
                        Log.d("comprimento ", ""+objects.size());

                        mErrorNoResult.setVisibility(View.GONE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        mPosts.clear();
                        mPosts.addAll(objects);
                        mSearchAdapter.notifyDataSetChanged();
                        mProgressBarUi.setVisibility(View.GONE);

                    }else {

                        mErrorNoResult.setVisibility(View.VISIBLE);
                        mErrorInternet.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"algo deu mal "+newText,Toast.LENGTH_LONG).show();
                        mTextNotFoud.setText("For " +newText);
                        mPosts.clear();
                        mProgressBarUi.setVisibility(View.GONE);

                    }
                }else  {

                    if (e.getCode() == ParseException.CONNECTION_FAILED) {

                        // No internet error
                        mErrorInternet.setVisibility(View.VISIBLE);
                        mErrorNoResult.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                        mProgressBarUi.setVisibility(View.GONE);


                    } else {

                        // Something was wrong
                        String error = e.getLocalizedMessage();

                    }

                }

            }
        });

        query1.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {

                    String counted = String.valueOf(count);
                    mPesquisaCount.setText(counted);

                }
            }
        });

        return false;
    }
}
