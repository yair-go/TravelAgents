package com.example.ezras.travelagencies.model.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Ezra_Steinmetz on August 2016
 *
 * the class offers storing and retrieving user's ID for the next time the app runs
 */
public class SaveSharedPreference {

    static final String PREF_USER_ID = "user-id";

    /**
     * this function saves the user ID into the shared preference - indicates that the associated
     * user is currently logged-in to the app
     * @param context a context to use to get access to the shared preference
     * @param userID - the user ID to save
     */
    public static void setUserID(Context context, int userID){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(PREF_USER_ID, userID).apply();
    }

    /**
     *
     * @param context a context to use to get access to the shared preference
     * @return the saved user ID or -1 if there's no saved user ID
     */
    public static int getUserID(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_USER_ID, -1);
    }

    /**
     * this function clears the
     * @param context a context to use to get access to the shared preference
     */
    public static void clear(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(PREF_USER_ID).apply();
    }
}
