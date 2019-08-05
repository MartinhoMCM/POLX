package com.example.appauth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appauth.R;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.ParseFile;

import java.util.ArrayList;


public class SlideAdapter extends PagerAdapter {

   Context context;
   LayoutInflater inflater;
   User mUserId;

   private ArrayList<Posts> postsArrayList;
   private ArrayList<ParseFile> parseFiles;
   Posts posts;

    public SlideAdapter(Context context, Posts posts ) {
        this.context = context;
        this.posts=posts;

    }

    public SlideAdapter(Context context, ArrayList<ParseFile> parseFiles) {
        this.context = context;
        this.parseFiles = parseFiles;
    }

    @Override
    public int getCount() {
        return parseFiles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view ==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

       // Model model  = parseFiles.get(position);
       // Posts posts = postsArrayList.get(position);

        inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.slide_layout, container, false);

        //TextView textView = view.findViewById(R.id.Hello);

          ImageView postImage =view.findViewById(R.id.product_image);


     if(parseFiles.get(position)!=null) {
         //String photo =posts.getImagePostArrayList().get(position).getUrl();
         Glide.with(context).load(parseFiles.get(position).getUrl()).into(postImage);
     }
     else {
         Glide.with(context).load(R.drawable.tres).into(postImage);
     }

        container.addView(view);
     return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }


}
