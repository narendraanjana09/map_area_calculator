package com.nsa.mapsspeech.Services;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

public class Common {
    public static final String KEY_REQUESTING_LOCATION_UPDATES ="Location Update Enable" ;

    public static String getLocationText(String text) {
        return new StringBuilder()
                .append("Real time location share is on!"+text)
                .toString();
    }

    public static CharSequence getLocationTitle(BackgroundService backgroundService) {
        return String.format("Location", DateFormat.getDateInstance().format(new Date()));
    }

    public static void setRequestLocationUpdates(Context context, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES,b)
                .apply();
    }

    public static boolean requestingLocationUpdates(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES,false);
    }
}
