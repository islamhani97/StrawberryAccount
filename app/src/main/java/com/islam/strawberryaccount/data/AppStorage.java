package com.islam.strawberryaccount.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.islam.strawberryaccount.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppStorage {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Inject
    public AppStorage(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

//    public void setUser(User user) {
//        sharedPreferences.edit().putString(Constants.SH_PREFS_USER, gson.toJson(user)).apply();
//    }
//
//    public User getUser() {
//        String user = sharedPreferences.getString(Constants.SH_PREFS_USER, Constants.NONE);
//        if (!user.equals(Constants.NONE)) {
//            return gson.fromJson(user, User.class);
//        } else {
//            return null;
//        }
//
//    }


    public void setLanguage(String language) {
        sharedPreferences.edit().putString(Constants.SH_PREFS_LANGUAGE, language).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(Constants.SH_PREFS_LANGUAGE, Constants.NONE);
    }


    public void setSignInStatus(boolean signInStatus) {
        sharedPreferences.edit().putBoolean(Constants.SH_PREFS_SIGN_IN, signInStatus).apply();
    }

    public boolean getSignInStatus() {
        return sharedPreferences.getBoolean(Constants.SH_PREFS_SIGN_IN, false);
    }
}
