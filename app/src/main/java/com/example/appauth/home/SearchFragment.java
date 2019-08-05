package com.example.appauth.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appauth.R;
import com.example.appauth.adapter.SearchAdapter;
import com.example.appauth.models.Posts;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, SearchAdapter.onItemClickListener {

    private ArrayList<Posts> mPosts;
    private SearchAdapter mSearchAdapter;
    private RecyclerView mRecyclerView;
    SearchView searchView;
    private LinearLayout mErrorInternet,mErrorNoResult;
    private TextView mTextNotFoud,mPesquisaCount;
    private ProgressBar mProgressBarUi;


    public SearchFragment() {
        // Required empty public constructor

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search, container, false);

        setHasOptionsMenu(true);

        mPosts = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(getActivity(), mPosts);
        mRecyclerView = v.findViewById(R.id.listaUp);
        mErrorNoResult = v.findViewById(R.id.noResults);
        mTextNotFoud = v.findViewById(R.id.id_text_field);
        mPesquisaCount = v.findViewById(R.id.pesquisaCount);
        mErrorInternet = v.findViewById(R.id.noInternet);
        mProgressBarUi = v.findViewById(R.id.progress_bar_id);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mSearchAdapter.onItemClickListener(SearchFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSearchAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Pesquisar ... ");

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {


        CharSequence text = searchView.getQuery();

        return true;
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
                        Toast.makeText(getContext(),"algo deu mal "+newText,Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public void onItemClick(int position) {

        Posts posts = mPosts.get(position);

        if (posts.getObjectId() != null){

            Intent intent = new Intent(getContext(), ProductDescriptionActivity.class);
            intent.putExtra(ProductDescriptionActivity.ObjectId, posts.getObjectId());
            startActivity(intent);
        }

    }


}
