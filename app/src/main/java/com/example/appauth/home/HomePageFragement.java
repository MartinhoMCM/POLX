package com.example.appauth.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appauth.R;
import com.example.appauth.adapter.PostsAdapter;
import com.example.appauth.buy.BuyConfirmActivity;
import com.example.appauth.models.Buy;
import com.example.appauth.models.Posts;
import com.google.android.material.chip.Chip;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HomePageFragement extends Fragment implements PostsAdapter.onItemClickListener, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Posts> mPosts;
    private PostsAdapter mPostsAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorInternet,mErrorNoResult;
    private TextView mCountPost;
    private ProgressBar mProgressCategoria;
    private View v;
    NestedScrollView mShimmir;

    private ArrayList<Buy> pendetes;

    public HomePageFragement() {
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle b) {
        super.onViewCreated(view, b);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.homepage_fragment, container, false);



        //Chips
        ChipsDec();

        //Error search info
        mErrorInternet = v.findViewById(R.id.noInternet);
        mErrorNoResult = v.findViewById(R.id.noResults);
        mCountPost = (TextView) v.findViewById(R.id.numeroPost);
        mShimmir = (NestedScrollView) v.findViewById(R.id.shimmer_home);
        mProgressCategoria = (ProgressBar) v.findViewById(R.id.progress_categoria_id);

        //Pendentes
        pendetes = new ArrayList<>();
        mPosts = new ArrayList<>();
        mPostsAdapter = new PostsAdapter(getActivity(), mPosts, pendetes);

        //RecyclerView para os posts
        mRecyclerView = v.findViewById(R.id.listaPosts);
        mRecyclerView.setAdapter(mPostsAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);
        mPostsAdapter.onItemClickListener(HomePageFragement.this);


        // mPostsAdapter = new PostsAdapter(getActivity(), mPosts);



        //SwiperRefresh
        setHasOptionsMenu(true);
        swipeRefreshLayout = v.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                mShimmir.setVisibility(View.GONE);
                mErrorNoResult.setVisibility(View.GONE);
                HomePageFragement.this.loadPosts();


            }
        });

        return v;

    }

    private void ChipsDec() {

        Chip idClothes = v.findViewById(R.id.idClothes);
        Chip idGames = v.findViewById(R.id.idGames);
        Chip idJobs = v.findViewById(R.id.idJobs);
        Chip idCosmetic = v.findViewById(R.id.idCosmetic);
        Chip idHome = v.findViewById(R.id.idHome);
        Chip idTech = v.findViewById(R.id.idTech);
        Chip idCar = v.findViewById(R.id.idCar);
        Chip idService = v.findViewById(R.id.idServices);
        Chip idOtherSells = v.findViewById(R.id.idOthersSells);

        idClothes.setOnClickListener(this);
        idGames.setOnClickListener(this);
        idJobs.setOnClickListener(this);
        idCosmetic.setOnClickListener(this);
        idHome.setOnClickListener(this);
        idTech.setOnClickListener(this);
        idCar.setOnClickListener(this);
        idService.setOnClickListener(this);
        idOtherSells.setOnClickListener(this);
    }

    private void loadPosts(){

        Buy buy = new Buy();
        ParseQuery<Buy> queryBuy = buy.geBuyParseQuery();
        queryBuy.findInBackground(new FindCallback<Buy>() {
            @Override
            public void done(List<Buy> objects, ParseException e) {
                if(e==null)
                {
                    pendetes.clear();
                    pendetes.addAll(objects);
                    mPostsAdapter.notifyDataSetChanged();
                }
            }
        });
        //

        ParseQuery<Posts> postsParseQuery = Posts.getQuery();
        postsParseQuery.whereExists(Posts.COL_IMAGE);
        postsParseQuery.whereExists(Posts.COL_AUTHOR);
        postsParseQuery.include(Posts.COL_AUTHOR);
        postsParseQuery.whereExists(Posts.COL_LOCALIZATION);
        postsParseQuery.orderByDescending(Posts.COL_CREATED_AT);

        postsParseQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {

                    String counted = String.valueOf(count);
                    mCountPost.setText(counted);

                }
            }
        });

        postsParseQuery.findInBackground(new FindCallback<Posts>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<Posts> usersNear, ParseException e) {

                if (usersNear != null) {

                    if (usersNear.size() > 0) {

                        mRecyclerView.setVisibility(View.VISIBLE);
                        mErrorInternet.setVisibility(View.GONE);
                        mErrorNoResult.setVisibility(View.GONE);
                        mShimmir.setVisibility(View.GONE);
                        mPosts.clear();
                        mPosts.addAll(usersNear);
                        mPostsAdapter.notifyDataSetChanged();

                    } else {

                        mPosts.clear();
                        mErrorNoResult.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mShimmir.setVisibility(View.GONE);

                        // There is no Post.
                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                        mShimmir.setVisibility(View.GONE);

                    }

                } else {

                    if (e.getCode() == ParseException.CONNECTION_FAILED) {

                        // No internet error
                        mErrorInternet.setVisibility(View.VISIBLE);
                        mErrorNoResult.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                        mShimmir.setVisibility(View.GONE);


                    } else {

                        // Something was wrong
                        String error = e.getLocalizedMessage();

                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                        mShimmir.setVisibility(View.GONE);
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
    public void onClick(View v) {
        mProgressCategoria.setVisibility(View.VISIBLE);

        ParseQuery<Posts> queries;

        ParseQuery<Posts> query1= Posts.getQuery();
        ParseQuery<Posts> query2= Posts.getQuery();

        int id =v.getId();

        switch (id)
        {
            case R.id.idClothes:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.close));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.fashion));

            case R.id.idGames:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.game));
                break;

            case R.id.idJobs:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.jobs));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.service));
                break;

            case R.id.idCosmetic:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.cosmetic));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.fashion));
                break;

            case R.id.idHome:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.home1));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.service));
                break;

            case R.id.idTech:
                query1.whereEqualTo(Posts.CATEGORY, v.getResources().getString(R.string.tel));
                query2.whereEqualTo(Posts.CATEGORY, v.getResources().getString(R.string.tech));

                break;

            case R.id.idCar:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.automobile));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.service));
                break;

            case R.id.idServices:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.automobile));
                query2.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.service));
                break;

            case R.id.idOthersSells:
                query1.whereMatches(Posts.CATEGORY, v.getResources().getString(R.string.others));
                break;

            default:
                return;

        }
        if(v.getId() ==R.id.idCar || v.getId()==R.id.idJobs || v.getId()==R.id.idClothes || v.getId()==R.id.idGames ||
                v.getId()==R.id.idCosmetic || v.getId()== R.id.idHome || v.getId()==R.id.idServices || v.getId() ==R.id.idTech
                || v.getId() ==R.id.idOthersSells)
        {
            List<ParseQuery<Posts>> queryList = new ArrayList<>();
            queryList.add(query1);
            queryList.add(query2);

            queries =ParseQuery.or(queryList);

            queries.findInBackground(new FindCallback<Posts>() {
                @Override
                public void done(List<Posts> postQueries, ParseException e) {
                    if(postQueries!=null){
                        if(postQueries.size()>0)
                        {
                            mPosts.clear();
                            mPosts.addAll(postQueries);
                            mPostsAdapter.notifyDataSetChanged();
                            mProgressCategoria.setVisibility(View.GONE);
                        }
                        else {
                            mProgressCategoria.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Not found", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
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
