package com.example.appauth.models;

import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ParseClassName("favority")
public class Favority extends ParseObject {

  public static String COL_AUTHOR = "author";
  public static String COL_TO_AUTHOR = "toAuthor";
  public static String COL_NOTI_TYPE = "notiType";
  public static String COL_POST = "post";
  public static String COL_CREATED_AT = "createdAt";
  public static String COL_OBJECT_ID = "objectId";



  public void setPost(Posts post){
    this.put(COL_POST, post);
  }

  public Posts getPost(){
    return (Posts) this.getParseObject(COL_POST);
  }

  public String getColNotiType() {
    return getString(COL_NOTI_TYPE);
  }

  public void setColNotiType(String value) {
    put(COL_NOTI_TYPE, value);
  }


  public void setAuthor(User author){
    this.put(COL_AUTHOR, author);
  }

  public User getAuthor(){
    return (User)this.getParseObject(COL_AUTHOR);
  }


  public void setToAuthor(User author){
    this.put(COL_TO_AUTHOR, author);
  }

  public User getToAuthor(){
    return (User)this.getParseObject(COL_TO_AUTHOR);
  }


  public String getTime(){

    Date messageDate = this.getCreatedAt();
    SimpleDateFormat sdf = null;

    //TimeAgo timeAgo = new TimeAgo();


    if(isToday(messageDate)){
      //sdf = new SimpleDateFormat("Today" + " "+ "HH:mm");
      sdf = new SimpleDateFormat("HH:mm");
    } else {
      sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
      //sdf = new SimpleDateFormat("dd MMM");
    }
    String time = sdf.format(messageDate);
    //return timeAgo.getTimeAgo(messageDate);

    return time;
  }

  public String getTimeShort(){

    Date messageDate = this.getCreatedAt();
    SimpleDateFormat sdf = null;

    //TimeAgo timeAgo = new TimeAgo();


    if(isToday(messageDate)){
      //sdf = new SimpleDateFormat("Today" + " "+ "HH:mm");
      sdf = new SimpleDateFormat("HH:mm");
    } else {
      sdf = new SimpleDateFormat("dd MMMM");
      //sdf = new SimpleDateFormat("dd MMM");
    }
    String time = sdf.format(messageDate);
    //return timeAgo.getTimeAgo(messageDate);

    return time;
  }


  public boolean isToday(Date date){
    Calendar calendar = Calendar.getInstance();
    Date today = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    return TextUtils.equals(sdf.format(today), sdf.format(date));
  }

  public static ParseQuery<Favority> getQuery() {
    return ParseQuery.getQuery(Favority.class);
  }


}
