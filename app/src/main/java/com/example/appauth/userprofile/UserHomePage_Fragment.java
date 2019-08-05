package com.example.appauth.userprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.home.MyAnuncioActivity;
import com.example.appauth.models.User;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomePage_Fragment extends Fragment {

    private TextView mUsername,mEmailUser;
    private User mCurrentUser;
    private CircleImageView mProfilePhoto;

    public UserHomePage_Fragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userpage_fragment, container, false);

        mUsername = view.findViewById(R.id.UserProfileName);
        mProfilePhoto = (CircleImageView)view.findViewById(R.id.UserImageProfile);
        mEmailUser = (TextView) view.findViewById(R.id.UserEmail);
        CardView mBtnEdit = (CardView) view.findViewById(R.id.editar_perfil);
        CardView mBtnEditPost = (CardView) view.findViewById(R.id.meus_anuncio_id);
        mCurrentUser = (User) ParseUser.getCurrentUser();

        if(mCurrentUser != null) {
            mAtualizaUserImage();
        }

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });

        mBtnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyAnuncioActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



    @SuppressLint("SetTextI18n")
    private void mAtualizaUserImage() {

        if (mCurrentUser != null) {
            mUsername.setText(mCurrentUser.getUserFirstName() + " " + mCurrentUser.getUserLastName());
            mEmailUser.setText(mCurrentUser.getEmails().trim());

        }

        if (mCurrentUser.getPhotoUrl().isEmpty()) {
            mProfilePhoto.setImageResource(R.drawable.placeholder);
        } else {

            Glide.with(this)
                    .load(mCurrentUser.getPhotoUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(mProfilePhoto);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mCurrentUser!=null) {
            mAtualizaUserImage();
        }
    }
}
