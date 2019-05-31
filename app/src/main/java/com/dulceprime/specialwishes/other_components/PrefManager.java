package com.dulceprime.specialwishes.other_components;

/**
 * Created by Nelson on 8/10/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nelson 12/12/2017.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "auto_birthday_pref";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private String IS_PRO_USER = "IsProUser";
    private String IS_FREE_USER = "IsFreeUser";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setIsFreeUser(boolean isFreeUser) {
        editor.putBoolean(IS_FREE_USER, isFreeUser);
        editor.commit();
    }

    public boolean isFreeUser() {
        return pref.getBoolean(IS_FREE_USER, true);
    }

    public void setIsProUser(boolean isProUser) {
        editor.putBoolean(IS_PRO_USER, isProUser);
        editor.commit();
    }

    public boolean isProUser() {
        return pref.getBoolean(IS_PRO_USER, false);
    }
}