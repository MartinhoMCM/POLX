package com.example.appauth.models;

import android.text.TextUtils;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    // Personal Information
    public  static final String USER_ID = "objectId";
    private static final String USER_FIRST_NAME = "firstName";
    private static final String USER_LAST_NAME = "lastName";
    public  static final String USER_NAME = "username";
    public  static final String NUMBER = "Number";

    // Security
    private static final String USER_EMAIL = "email";
    private static final String USER_EMAILS = "emails";
    private static final String USER_EMAIL_VERIFIED = "emailVerified";


    // Media

    public static final String USER_PHOTO = "avatar";
    public static final String USER_PHOTO_THUMB = "avatar_thumb";
    public static final String USER_COVER = "cover";


    //////////////////////////////////////////////////////////////////////////////////////////////

    // Personal Information
    public String getUserFirstName() {
        return getString(USER_FIRST_NAME);
    }

    public void setUserFirstName(String firstName) {
       put(USER_FIRST_NAME, firstName);
    }

    //last name
    public String getUserLastName() {
        return getString(USER_LAST_NAME);
    }

    public void setUserLastName(String lastName) {
       put(USER_LAST_NAME, lastName);
    }

    //number for contact
    public String getNumber() {
        return getString(NUMBER);
    }

    public void setNumber(String number) {
        put(NUMBER, number);
    }

    // Security

    public String getEmail(){
        return getString(USER_EMAIL);
    }

    public String getEmails(){
        return getString(USER_EMAILS);
    }


    public void setEmail(String email){
        put(USER_EMAIL, email);
    }

    public void setEmails(String email){
        put(USER_EMAILS, email);
    }


    public String getVerified(){
        if(TextUtils.equals(this.getString(USER_EMAIL_VERIFIED), "true")){
            return "verified";
        } else if(TextUtils.equals(this.getString(USER_EMAIL_VERIFIED), "false")){
            return "unverified";
        } else {
            return "unverified";
        }
    }

    public boolean isVerified() {
        String Verified = getString(USER_EMAIL_VERIFIED);
        return Verified != null && TextUtils.equals(Verified, "true");
    }

    // Media

    public String getPhotoUrl(){
        try {
            //String getPhotoUrl = fetchIfNeeded().getString("photo");
            fetchIfNeeded().getString("avatar");
        } catch (ParseException e) {
            //Log.e(TAG, "Something has gone terribly wrong with Parse", e);
        }

        ParseFile photoFile = getParseFile("avatar");
        if(photoFile != null)
            return getParseFile("avatar").getUrl();
        else
            return "";
    }

    public String getPhotoThumbUrl(){
        try {
            //String getPhotoUrl = fetchIfNeeded().getString("photo");
            fetchIfNeeded().getString("avatar_thumb");
        } catch (ParseException e) {
            //Log.e(TAG, "Something has gone terribly wrong with Parse", e);
        }

        ParseFile photoFile = getParseFile("avatar_thumb");
        if(photoFile != null)
            return getParseFile("avatar_thumb").getUrl();
        else
            return "";
    }

    public String getCoverUrl(){
        ParseFile photoFile = getParseFile("cover");
        if(photoFile != null)
            return getParseFile("cover").getUrl();
        else
            return "";
    }

    public void getAvatar(GetDataCallback callback){
        getParseFile(USER_PHOTO).getDataInBackground(callback);
    }

    public void getCover(GetDataCallback callback){
        getParseFile(USER_COVER).getDataInBackground(callback);
    }

    public void getAvatarThumb(GetDataCallback callback){
        getParseFile(USER_PHOTO_THUMB).getDataInBackground(callback);
    }

    public void setProfilePhoto(ParseFile file) {
        put(USER_PHOTO, file);
    }

    public void setCoverPhoto(ParseFile file) {
        put(USER_COVER, file);
    }

    public void setProfilePhotoThumb(ParseFile file) {
        put(USER_PHOTO_THUMB, file);
    }


    ////////////////////////////////////////////////////////////////////

    // Settings
    public static User getUser(){
        return (User) ParseUser.getCurrentUser();
    }

    public static ParseQuery<User> getUserQuery(){
        return ParseQuery.getQuery(User.class);
    }
}
