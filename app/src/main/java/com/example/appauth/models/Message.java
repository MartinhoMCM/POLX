package com.example.appauth.models;

import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String SENDER_AUTHOR = "fromUser";
    public static final String SENDER_AUTHOR_ID = "fromUserId";
    //toUser
    public static final String RECEIVER_AUTHOR = "toUser";
    public static final String RECEIVER_AUTHOR_ID = "toUserId";

    public static final String MESSAGE = "message";

    public User getSenderAuthor() {
        return (User) getParseObject(SENDER_AUTHOR);
    }

    public void setSenderAuthor(User senderAuthor){
        put(SENDER_AUTHOR, senderAuthor);
    }

    public User getReceiverAuthor() {
        return (User) getParseObject(RECEIVER_AUTHOR);
    }

    //I change User receiverAuthor to
    public void setReceiverAuthor(User receiverAuthor){
        put(RECEIVER_AUTHOR, receiverAuthor);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }


    public void setMessage(String message){
        put(MESSAGE, message);
    }

    public String getSenderAuthorId() {
        return getString(SENDER_AUTHOR_ID);
    }

    public void setSenderAuthorId(String senderAuthorId){
        put(SENDER_AUTHOR_ID, senderAuthorId);
    }

    public String getReceiverAuthorId() {
        return getString(RECEIVER_AUTHOR_ID);
    }

    public void setReceiverAuthorId(String receiverAuthorId){
        put(RECEIVER_AUTHOR_ID, receiverAuthorId);
    }

    public  ParseQuery<Message> getMessageParseQuery()
    {
         return  ParseQuery.getQuery(Message.class);
    }
}
