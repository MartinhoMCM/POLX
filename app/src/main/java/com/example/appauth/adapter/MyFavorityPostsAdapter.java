package com.example.appauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.home.FavorityActivity;
import com.example.appauth.home.MyAnuncioActivity;
import com.example.appauth.home.ProductDescriptionActivity;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyFavorityPostsAdapter extends RecyclerView.Adapter<MyFavorityPostsAdapter.mPostViewHolder> {
    private List<Favority> favorities;
    private Activity mContext;
    private User mCurrentUser;
    private onItemClickListener mListener;
    public static String ObjectId = "objectId";
    private Posts mPosts;

    public MyFavorityPostsAdapter(Activity context, List<Favority> favorityList) {
        favorities = favorityList;
        mContext = context;
    }

    public void onItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public mPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_card_favority_post, parent, false);

        return new mPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final mPostViewHolder viewHolder, final int position) {

        final Favority favority = favorities.get(position);

        mCurrentUser = (User) User.getCurrentUser();
        if (favority != null)
        {

            viewHolder.mTitle.setText(favority.getPost().getTitle());
            viewHolder.mDescription.setText(favority.getPost().getDescription());
            viewHolder.mLocalization.setText(favority.getPost().getLocalization());
            viewHolder.mCategory.setText(favority.getPost().getCATEGORY());
           // viewHolder.mDataView.setText(favority.getPost().getTimeShort());
            viewHolder.mPrice.setText(favority.getPost().getPrice());


        }

        if (favority.getAuthor().getPhotoUrl().isEmpty()) {

            viewHolder.mImageUser.setImageResource(R.drawable.placeholder);

        } else {

            Glide.with(mContext)
                    .load(favority.getAuthor().getPhotoUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.mImageUser);

        }

        //////////////////////////////////////////////////////
        if (favority.getPost().getPostImageUrl().isEmpty()) {

            viewHolder.PostImage.setImageResource(R.drawable.placeholder);

        } else {

            Glide.with(mContext)
                    .load(favority.getPost().getPostImageUrl())
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.PostImage);

         }


        ParseQuery<Posts> CheckFavority = Posts.getQuery();
        CheckFavority.getFirstInBackground(new GetCallback<Posts>() {
            @Override
            public void done(Posts mpost, ParseException e) {

                if (mpost != null){

                    MyFavorityPostsAdapter.this.mPosts = mpost;
                    mCurrentUser =(User) ParseUser.getCurrentUser();

                    Log.d("temos o valor do id  ", "é " +mpost);

                    ParseQuery<Favority> favorityQuery =  Favority.getQuery();

                    favorityQuery.whereEqualTo(Favority.COL_POST, mpost);
                    favorityQuery.whereEqualTo(Favority.COL_AUTHOR, mCurrentUser);
                    favorityQuery.getFirstInBackground(new GetCallback<Favority>() {
                        @Override
                        public void done(Favority object, ParseException e) {

                            if (e == null){

                                Log.d("adicinou ", "" +object);
                                viewHolder.mFavorityPost.setImageResource(R.drawable.favorito_bold);
                                notifyDataSetChanged();

                            }
                        }
                    });
                }
            }
        });

        viewHolder.mFavorityPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.mFavorityPost.setImageResource(R.drawable.ic_favority_bold);

                ParseQuery<Favority> FavorityQuery = Favority.getQuery();
                User currentUser = (User)User.getCurrentUser();

                FavorityQuery.whereEqualTo(Favority.COL_AUTHOR, currentUser);
                FavorityQuery.whereEqualTo(Favority.COL_POST, mPosts);
                FavorityQuery.getFirstInBackground(new GetCallback<Favority>() {
                    @Override
                    public void done(Favority update, ParseException e) {
                        if (e == null) {

                            update.deleteInBackground(new DeleteCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {

                                        viewHolder.mFavorityPost.setImageResource(R.drawable.ic_favority_outline);
                                        Toast.makeText(mContext, "Removido com sucesso", Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();
                                        mContext.startActivity(new Intent(mContext.getApplicationContext(), FavorityActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                                    } else {

                                        // not deleted
                                        Toast.makeText(mContext, "not deleted "+e, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                    }
                });

            }

        });


        viewHolder.mExpandirBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mAblitarPreco.setVisibility(View.VISIBLE);
                viewHolder.mAblitarLocalizacao.setVisibility(View.VISIBLE);
                viewHolder.mExpandirBottom.setVisibility(View.GONE);
                viewHolder.mExpandirTop.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.mExpandirTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.mAblitarPreco.setVisibility(View.GONE);
                viewHolder.mAblitarLocalizacao.setVisibility(View.GONE);
                viewHolder.mExpandirBottom.setVisibility(View.VISIBLE);
                viewHolder.mExpandirTop.setVisibility(View.GONE);
            }
        });

     }

    @Override
    public int getItemCount() {
        return favorities.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

    public class mPostViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDescription,mLocalization,mCategory,mDataView,mPrice;
        ImageView PostImage;
        ImageButton mExpandirTop,mExpandirBottom,mFavorityPost;
        LinearLayout mAblitarPreco,mAblitarLocalizacao;
        CircleImageView mImageUser;

        public mPostViewHolder(@NonNull View v) {
            super(v);

            mTitle = (TextView) v.findViewById(R.id.title_titulo);
            mDescription = (TextView) v.findViewById(R.id.description);
            mLocalization = (TextView) v.findViewById(R.id.location_id);
            mCategory = (TextView) v.findViewById(R.id.categoria_id);
            mDataView = (TextView) v.findViewById(R.id.data_id);
            mPrice = (TextView) v.findViewById(R.id.preco_id);

            mImageUser = (CircleImageView) v.findViewById(R.id.user_img);
            PostImage = (ImageView) v.findViewById(R.id.imgCapa);

            mExpandirBottom = (ImageButton) v.findViewById(R.id.expandir_bottom_id) ;
            mExpandirTop = (ImageButton) v.findViewById(R.id.expandir_top_id) ;
            mAblitarPreco = (LinearLayout) v.findViewById(R.id.ablitar_preco);
            mAblitarLocalizacao = (LinearLayout) v.findViewById(R.id.ablitar_localizacao);
            mFavorityPost = (ImageButton) v.findViewById(R.id.id_favorito_like);

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
