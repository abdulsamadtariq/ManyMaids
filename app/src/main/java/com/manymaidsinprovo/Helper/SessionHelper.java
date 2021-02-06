package com.manymaidsinprovo.Helper;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.manymaidsinprovo.Model.User;

public class SessionHelper {


    public static void createUserSession(Context context, User user){

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString("current_user",new Gson().toJson(user))
        .apply();
    }

    public static void logout(Context context){

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove("current_user")
                .apply();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove("userRole")
                .apply();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove("IsGuest")
                .apply();
    }
    public static boolean isUserLoggedIn(Context context){


        String currentUser=PreferenceManager.getDefaultSharedPreferences(context).getString("current_user",null);
        return currentUser != null;
    }

    public static User getCurrentUser(Context context){
        String currentUser=PreferenceManager.getDefaultSharedPreferences(context).getString("current_user",null);
        return new Gson().fromJson(currentUser,User.class);
    }

    public static void setUserRole(Context context,int userRole){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putInt("userRole",userRole)
                .apply();
    }
    public static int getUserRole(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("userRole",-1);
    }

    public static void setGuest(Context context,boolean IsGuest){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean("IsGuest",IsGuest)
                .apply();
    }
    public static boolean getGuest(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("IsGuest",false);
    }
}
