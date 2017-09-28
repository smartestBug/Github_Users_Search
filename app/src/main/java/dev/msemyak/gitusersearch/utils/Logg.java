package dev.msemyak.gitusersearch.utils;

import android.util.Log;

public class Logg {

    final static String TAG = "GU";
    final static String MSG_PREFIX = "##### " + TAG + " ##### ";

    public static void Logg(String str) {
        str = MSG_PREFIX + str;
        Log.d(TAG, str);
    }

    public static void Logg(String str, Object ... args) {
        str = MSG_PREFIX + str;
        Log.d(TAG, String.format(str, args));
    }
}
