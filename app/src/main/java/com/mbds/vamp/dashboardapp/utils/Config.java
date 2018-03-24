package com.mbds.vamp.dashboardapp.utils;

/**
 * Created by Yasmine on 15/03/2018.
 */

public class Config {

    // WEB Server URL
    private static final String WEB_SERVER_URL = "http://192.168.43.247:8080/api/";
    // Socket Server URL
    public static final String SOCKET_SERVER_URL = "http://192.168.43.247:8080";

    //URL to login
    public static final String LOGIN_URL = WEB_SERVER_URL + "login";

    //JSON URL
    public static final String USERS_URL = WEB_SERVER_URL + "user.json";
    public static final String CARS_URL = WEB_SERVER_URL + "car";
    public static final String PROFIL_URL = WEB_SERVER_URL + "profile";
    public static final String PLAYLIST_URL = WEB_SERVER_URL + "playList";
    public static final String MEDIA_URL = WEB_SERVER_URL + "media";

    //Keys for email and password
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //Keys for Sharedpreferences
    //shared preferences name
    public static final String SHARED_PREF_NAME = "vampapp";
    //the username of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";
    //the password  of current logged in user
    public static final String PASSWORD_SHARED_PREF = "password";
    //access token
    public static final String ACCESS_TOKEN_SHARED_PREF = "";
    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

}
