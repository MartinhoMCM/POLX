package com.example.appauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Buy;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private List<Posts> mPosts;
    private Activity mContext;
    private User mCurrentUser;
    private onItemClickListener mListener;
    public Posts myposts;
    public ArrayList<Buy>buyArrayList;

    public PostsAdapter(Activity context, List<Posts> posts) {
        mPosts = posts;
        mContext = context;
    }

    public PostsAdapter(Activity context, List<Posts> posts, ArrayList<Buy> buys)
    {
        mPosts = posts;
        mContext = context;
        buyArrayList =buys;

    }

    public void onItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_card_post, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Posts posts = mPosts.get(position);

        mCurrentUser = (User) User.getCurrentUser();
        if (posts != null)
        {

            viewHolder.mTitle.setText(posts.getTitle());
            viewHolder.mDescription.setText(posts.getDescription());
            viewHolder.mLocalization.setText(posts.getLocalization());
            viewHolder.mCategory.setText(posts.getCATEGORY());
            viewHolder.mPrice.setText(posts.getPrice());

            Date date = posts.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            viewHolder.mDataView.setText(simpleDateFormat.format(date));

            for (int i=0; i<buyArrayList.size(); i++)
            {
                if(posts.getObjectId().equals(buyArrayList.get(i).getPostId()))
                {

                    viewHolder.mBuy.setText("Pendente");
                    viewHolder.mBuy.setTextColor(Color.parseColor("#ffffbb33"));

                } }
        }

        if (posts.getAuthor().getPhotoUrl().isEmpty()) {

            viewHolder.mImageUser.setImageResource(R.drawable.placeholder);

        } else {

            Glide.with(mContext)
                    .load(posts.getAuthor().getPhotoUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.mImageUser);

        }

        //////////////////////////////////////////////////////
        if (posts.getPostImageUrl().isEmpty()) {

            viewHolder.PostImage.setImageResource(R.drawable.placeholder);

        } else {

            Glide.with(mContext)
                    .load(posts.getPostImageUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.PostImage);

        }

        PostsAdapter.this.myposts = posts;
        mCurrentUser =(User) ParseUser.getCurrentUser();
        Log.d("temos o valor do id  ", "é " +posts);

        ParseQuery<Favority> favorityQuery =  Favority.getQuery();

        favorityQuery.whereEqualTo(Favority.COL_POST, posts);
        favorityQuery.whereEqualTo(Favority.COL_AUTHOR, mCurrentUser);
        favorityQuery.getFirstInBackground(new GetCallback<Favority>() {
            @Override
            public void done(Favority object, ParseException e) {

                if (e == null){

                    Log.d("adicinou ", "" +object);
                    viewHolder.mFavoritoPost.setImageResource(R.drawable.favorito_bold);
                    notifyDataSetChanged();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDescription,mLocalization,mCategory,mDataView,mPrice, mBuy ;
        ImageView PostImage,mFavoritoPost;
        CircleImageView mImageUser;



        ViewHolder(View v) {
            super(v);

            mTitle = (TextView) v.findViewById(R.id.title_titulo);
            mDescription = (TextView) v.findViewById(R.id.description);
            mLocalization = (TextView) v.findViewById(R.id.location_id);
            mCategory = (TextView) v.findViewById(R.id.categoria_id);
            mDataView = (TextView) v.findViewById(R.id.data_id);
            mPrice = (TextView) v.findViewById(R.id.preco_id);
            mBuy =v.findViewById(R.id.mBuy);

            mImageUser = (CircleImageView) v.findViewById(R.id.user_img);
            PostImage = (ImageView) v.findViewById(R.id.imgCapa);
            mFavoritoPost = (ImageView) v.findViewById(R.id.id_favority_post);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}