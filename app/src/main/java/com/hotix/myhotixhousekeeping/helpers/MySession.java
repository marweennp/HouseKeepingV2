package com.hotix.myhotixhousekeeping.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {

    // All Shared Preferences Keys
    //MySession
    public static final String KEY_LOGIN = "Login";
    public static final String KEY_LOGEDIN = "Logedin";


    // Sharedpref file name
    private static final String PREF_NAME = "HouseKeeperSession";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public MySession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**********************************(  Getters & Setters )*************************************/
    // Strings
    public String getLogin() {
        return pref.getString(KEY_LOGIN, null);
    }

    public void setLogin(String login) {
        editor.putString(KEY_LOGIN, login);
        editor.commit();
    }
    // boolean
    public boolean getLogedin() {
        return pref.getBoolean(KEY_LOGEDIN, false);
    }

    public void setLogedin(boolean logedin) {
        editor.putBoolean(KEY_LOGEDIN, logedin);
        editor.commit();
    }


    /**********************************(  _______  )*************************************/
    //Clear session details
    public void clearSessionDetails() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }


}
