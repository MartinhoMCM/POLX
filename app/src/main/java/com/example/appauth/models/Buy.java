package com.example.appauth.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Buy")
public class Buy extends ParseObject {


    public static final String buyerID="buyerId";
    public static final String buyerInfo="buyerInfo";
    public static final String buyerEmail="buyerEmail";

    public static final String buyerFirstName="buyerFirstName";
    public static final String buyerSecondName="buyerSecondName";

    public static final String buyerProvince="buyerProvince";
    public static final String buyerCounty="buyerCountry";
    public static final String buyerAddress1="buyerAddress1";
    public static final String buyerAddress2 ="buyerAddress2";
    public static final String ticket ="ticket";

    public static final String buyerZipCode="buyerZipCode";

    public static final String productPrice="productPrice";
    public static final String productName="productName";

    public static final String postId="postId";


    public void setBuyerID(String Id)
    {
        this.put(buyerID, Id);

    }


    public void setBuyerInfo(User buyer)
    {
        this.put(buyerInfo, buyer);

    }

    public  String getPostId() {
        return  getString(postId);
    }

    public void setPostId(String Id)
    {
        this.put(postId, Id);

    }
    public void setBuyerEmail(String email)
    {
        this.put(buyerEmail, email);
    }

    public void setBuyerFirstName(String firstName)
    {
        this.put(buyerFirstName, firstName);

    }

    public void setBuyerLastName(String secondName)
    {
        this.put(buyerSecondName, secondName);

    }

    public  void setTicket(String bolderon)
    {
        put(this.ticket, bolderon);
    }

    public void setProductPrice(String price)
    {
        put(productPrice, price);

    }

    public void setProductName(String name)
    {
        put(productName, name);

    }


    public void setBuyerProvince(String province)
    {
        this.put(buyerProvince, province);
    }
    public void setBuyerCountry(String county)
    {
        this.put(buyerCounty, county);

    }
    public  void setBuyerAddress1(String address1)
    {
        this.put(buyerAddress2, address1);
    }

    public  void setBuyerAddress2(String address2)
    {
        this.put(buyerAddress2, address2);
    }

    public void setBuyerZipCode(String zipCode)
    {
        this.put(buyerZipCode, zipCode);

    }

    public  String getBuyerID() {
        return getString(buyerID) ;
    }

    public  String getBuyerInfo() {
        return buyerInfo;
    }

    public  String getBuyerEmail() {
        return getString(buyerEmail) ;
    }

    public  String getBuyerFirstName() {
        return getString(buyerFirstName) ;
    }

    public String getBuyerSecondName() {
        return getString(buyerSecondName) ;
    }

    public  String getBuyerProvince() {
        return getString(buyerProvince) ;
    }

    public String getBuyerAddress1() {
        return getString(buyerAddress1);
    }

    public  String getBuyerAddress2() {
        return getString(buyerAddress2) ;
    }

    public String getBuyerZipCode() {
        return getString(buyerZipCode) ;
    }

    public   static ParseQuery<Buy> getQuery()
    {
        return ParseQuery.getQuery(Buy.class);
    }

    public  String getBuyerCounty() {
        return getString(buyerCounty) ;
    }

    public  String getTicket() {
        return  getString(ticket);
    }

    public  String getProductPrice() {
        return getString(productPrice);
    }

    public String getProductName() {
        return  getString(productName);
    }


    public  ParseQuery<Buy> geBuyParseQuery()
    {
        return  ParseQuery.getQuery(Buy.class);
    }


};
