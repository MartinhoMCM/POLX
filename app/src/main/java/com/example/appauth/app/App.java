package com.example.appauth.app;

import android.app.Application;

import com.example.appauth.models.Buy;
import com.example.appauth.models.Favority;
import com.example.appauth.models.Message;
import com.example.appauth.models.Notifications;
import com.example.appauth.models.Posts;
import com.example.appauth.models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.livequery.ParseLiveQueryClient;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class App extends Application {
    public static ParseLiveQueryClient liveQueryClient;

    private static final String PARSE_APP_SERVER_URL = "https://parseapi.back4app.com/";  // Parse Server URL
    private static final String PARSE_APP_ID = "ACt12kIrV7ZTrGs9galQrQixHPQTrEC3xVN5OXyW";  // App ID  production
    private static final String PARSE_CLIENT = "NhYo3RH4sS1MhPxqC75JuKYumDt5E0dcy4FXdPie"; // Client ID production


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Posts.class);
        ParseObject.registerSubclass(Favority.class);
        ParseObject.registerSubclass(Notifications.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Buy.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                //As credencias e configuração da aplicação
                .applicationId(PARSE_APP_ID)
                .clientKey(PARSE_CLIENT)
                .server(PARSE_APP_SERVER_URL)
                .enableLocalDataStore()
                .build()
        );

        try {
            liveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://commerceapp.back4app.io"));
            liveQueryClient.connectIfNeeded();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public static ParseLiveQueryClient getLiveQueryClient (){

        return liveQueryClient;
    }
}
