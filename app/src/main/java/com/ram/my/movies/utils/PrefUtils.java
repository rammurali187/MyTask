package com.ram.my.movies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ram.my.movies.data.api.Sort;



public final class PrefUtils {


    public static final String PREF_BROWSE_MOVIES_MODE = "pref_browse_movies_mode";



    public static String getBrowseMoviesMode(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_BROWSE_MOVIES_MODE, Sort.POPULARITY.toString());
    }

    public static void setBrowseMoviesMode(final Context context, String mode) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_BROWSE_MOVIES_MODE, mode).apply();
    }

}
