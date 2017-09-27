package dev.msemyak.gitusersearch.utils;

import android.util.Log;

public class Logg {

    final static String TAG = "TAG";

    public static void Logg(String str) {
        Log.d(TAG, str);
    }

    public static void Logg(String str, Object ... args) {
        Log.d(TAG, String.format(str, args));
    }
}
