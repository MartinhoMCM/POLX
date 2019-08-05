package com.example.appauth.models;


import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ParseClassName("posts")
public class Posts extends ParseObject{

    public static String COL_AUTHOR = "author";
    public static  String COL_IMAGE = "avatar";
    public static String COL_IMAGES ="picture";

    public static String COL_DESCRIPTION = "text";
    public static String COL_TITLE ="title";
    public static String COL_TITLE_LOWER_CASE ="title_low_case";
    public static String COL_PRICE = "price";
    public static String COL_LOCALIZATION="localization";
    public static String CATEGORY="category";
    public static String COL_CREATED_AT = "createdAt";


    public  void setAuthor(User colAuthor) {
        this.put(COL_AUTHOR, colAuthor);
    }

    public void setImage(ParseFile colImage) {
       this.put(COL_IMAGE, colImage);

    }

    public  void setImageList(List<ParseFile> parseFiles)
    {
        this.put(COL_IMAGES, parseFiles);
    }



    public void setDescription(String text) {
        this.put(COL_DESCRIPTION, text);
    }

    public void setTitle(String title) {
       this.put(COL_TITLE, title);
        this.put(COL_TITLE_LOWER_CASE, title.toLowerCase());

    }
    public void setPrice(String price) {
        this.put(COL_PRICE, price);

    }


    public void setLocalization(String localization) {
        this.put(COL_LOCALIZATION, localization);
    }

    public void setCategory(String category) {
       this.put(CATEGORY, category);
    }

    public  String getCATEGORY() {
        return getString(CATEGORY);
    }

    public User getAuthor() {
        return (User) this.getParseObject(COL_AUTHOR);
    }

    public ParseFile getColImage() {
        return getParseFile(COL_IMAGE);
    }

    public String getDescription() {
        return getString(COL_DESCRIPTION);
    }

    public String getTitle() {
        return getString(COL_TITLE);
    }

    public String getPrice() {
        return getString(COL_PRICE);
    }

    public String getLocalization() {
        return getString(COL_LOCALIZATION);
    }

    public String getPostImageUrl() {

        String postImageUrl = "";
        try {
            //this.fetchIfNeeded().getParseFile(COL_IMAGE);
            if (this.fetchIfNeeded().getParseFile(COL_IMAGE) != null) {
                return this.getParseFile(COL_IMAGE).getUrl();
            } else {
                return postImageUrl;
            }

        } catch (ParseException e) {
            ///Log.v(LOG_TAG, e.toString());
            //e.printStackTrace();
        }

        return postImageUrl;
    }


    public List<ParseFile> getImagePostArrayList() {

       return this.getList(COL_IMAGES);
    }


    public static ParseQuery<Posts> getQuery() {
        return ParseQuery.getQuery(Posts.class);
    }


}
