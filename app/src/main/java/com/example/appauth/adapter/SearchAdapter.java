package com.example.appauth.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Posts> mPosts;
    private Activity mContext;
    private User mCurrentUser;
    private onItemClickListener mListener;

    public SearchAdapter(Activity context, List<Posts> posts) {
        mPosts = posts;
        mContext = context;
    }

    public void onItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext(); LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_card_pesquisa, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Posts posts = mPosts.get(position);

        mCurrentUser = (User) User.getCurrentUser();
        if (posts != null)
        {
            viewHolder.mPesquisaTitle.setText(posts.getTitle());
            viewHolder.mPesquisaCategory.setText(posts.getCATEGORY());

            if (posts.getAuthor().getPhotoUrl().isEmpty()) {

                viewHolder.mPesquisaImageUser.setImageResource(R.drawable.placeholder);

            } else {

                Glide.with(mContext)
                        .load(posts.getAuthor().getPhotoUrl())
                        //.networkPolicy(NetworkPolicy.OFFLINE)
                        .error(R.drawable.placeholder)
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.mPesquisaImageUser);

            }
            //////////////////////////////////////////////////////capa
            if (posts.getPostImageUrl().isEmpty()) {

                viewHolder.mPesquisaCapa.setImageResource(R.drawable.placeholder);

            } else {

                Glide.with(mContext)
                        .load(posts.getPostImageUrl())
                        //.networkPolicy(NetworkPolicy.OFFLINE)
                        .error(R.drawable.placeholder)
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.mPesquisaCapa);

            }


        }


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView mPesquisaTitle;
        TextView mPesquisaCategory;
        CircleImageView mPesquisaImageUser;
        ImageView mPesquisaCapa;

        ViewHolder(View v) {
            super(v);

            mPesquisaTitle = (TextView) v.findViewById(R.id.pesquisa_title);
            mPesquisaCategory = (TextView) v.findViewById(R.id.pesquisa_category);
            mPesquisaImageUser = (CircleImageView) v.findViewById(R.id.pesquisa_user_img);
            mPesquisaCapa = (ImageView) v.findViewById(R.id.pesquisa_img_capa);

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