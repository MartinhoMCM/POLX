package com.example.appauth.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.home.EditPostActivity;
import com.example.appauth.home.MyAnuncioActivity;
import com.example.appauth.home.ProductDescriptionActivity;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyUserPostsAdapter extends RecyclerView.Adapter<MyUserPostsAdapter.mPostViewHolder> {
    private List<Posts> mPosts;
    private Activity mContext;
    private User mCurrentUser;
    private onItemClickListener mListener;
    public static String ObjectId = "objectId";

    public MyUserPostsAdapter(Activity context, List<Posts> posts) {
        mPosts = posts;
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
        View contactView = inflater.inflate(R.layout.row_card_edit_post, parent, false);

        return new mPostViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final mPostViewHolder viewHolder, final int position) {

        final Posts posts = mPosts.get(position);

        mCurrentUser = (User) User.getCurrentUser();
        if (posts != null)
        {

            viewHolder.mTitle.setText(posts.getTitle());
            viewHolder.mDescription.setText(posts.getDescription());
            viewHolder.mLocalization.setText(posts.getLocalization());
            viewHolder.mCategory.setText(posts.getCATEGORY());
            //viewHolder.mDataView.setText(posts.getTimeShort());
            viewHolder.mPrice.setText(posts.getPrice());

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

        viewHolder.mDeletar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Alerta");
                    builder.setMessage("Voce deseja excluir esse anuncio?");
                    builder.setCancelable(false);

                    builder.setNegativeButton("NAO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ParseQuery<Posts> query = Posts.getQuery();

                            query.whereEqualTo(Posts.COL_AUTHOR, mCurrentUser);
                            query.whereExists(Posts.COL_IMAGE);
                            query.orderByDescending(Posts.COL_CREATED_AT);

                            query.findInBackground(new FindCallback<Posts>() {
                                @Override
                                public void done(List<Posts> objects, ParseException e) {
                                    if (e == null){
                                        if (objects.size() > 0){

                                            objects.get(0).deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        // Success
                                                        Toast.makeText(mContext, "Deletado com sucesso", Toast.LENGTH_LONG).show();
                                                        mContext.startActivity(new Intent(mContext.getApplicationContext(), MyAnuncioActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        notifyDataSetChanged();
                                                    } else {
                                                        // Failed
                                                        Toast.makeText(mContext, "Nao apagou"+e, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }
                    });
                    builder.show();

              }
        });


        viewHolder.mEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Posts posts = mPosts.get(position);

                if (posts.getObjectId() != null){

                    Intent intent = new Intent(mContext.getApplicationContext(), EditPostActivity.class);
                    intent.putExtra(ProductDescriptionActivity.ObjectId, posts.getObjectId());
                    mContext.startActivity(intent);
                }
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
        return mPosts.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

    public class mPostViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mDescription;
        TextView mLocalization;
        TextView mCategory;
        TextView mDataView;
        TextView mPrice;
        ImageView PostImage;
        ImageButton mEditar,mDeletar,mExpandirTop,mExpandirBottom;
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

            mEditar = (ImageButton) v.findViewById(R.id.editar_id) ;
            mDeletar = (ImageButton) v.findViewById(R.id.deletar_id) ;
            mExpandirBottom = (ImageButton) v.findViewById(R.id.expandir_bottom_id) ;
            mExpandirTop = (ImageButton) v.findViewById(R.id.expandir_top_id) ;
            mAblitarPreco = (LinearLayout) v.findViewById(R.id.ablitar_preco);
            mAblitarLocalizacao = (LinearLayout) v.findViewById(R.id.ablitar_localizacao);

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
