package com.wishhard.h24.shared_pref_util;

import android.content.Context;
import android.content.SharedPreferences;

public class TimeSharedPref {
    private static final String TIME_PREF_KEY = "com.wishhard.sw24.shared_pref_util_time_pref_key";


     private static TimeSharedPref mTimeSharedPref;
     private Context mContext;
     private SharedPreferences mSharedPreferences;
     private SharedPreferences.Editor mSharedPreferencesEditor;

     private TimeSharedPref(Context context) {
         mContext = context;

         mSharedPreferences = mContext.getSharedPreferences(TIME_PREF_KEY,Context.MODE_PRIVATE);
         mSharedPreferencesEditor = mSharedPreferences.edit();

     }


     public static synchronized TimeSharedPref getInstance(Context context) {

         if(mTimeSharedPref == null) {
               mTimeSharedPref =  new TimeSharedPref(context.getApplicationContext());
         }
         return mTimeSharedPref;
     }

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }


    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }

}
