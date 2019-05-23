package com.kv.swiggyaddress.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.gson.Gson;

public class LocalStorage {

    public static UserData getRecentAddress(Context context) {
        UserData userData = null;
        String data = getPreferences(context, "RECENT_ADDRESS");
        if (!data.equalsIgnoreCase("")) {
            userData = new Gson().fromJson(data, UserData.class);
        }
        return userData;
    }

    public static void setRecentAddress(Context con, String value) {
        setPreferences(con, "RECENT_ADDRESS", value);
    }

    public static void setLatLng(Context ctx, Location location) {
        setPreferences(ctx, "CURRENT_LAT", "" + location.getLatitude());
        setPreferences(ctx, "CURRENT_LNG", "" + location.getLongitude());
    }

    public static Location getLatLng(Context ctx) {
        String lat = getPreferences(ctx, "CURRENT_LAT");
        String lng = getPreferences(ctx, "CURRENT_LNG");

        Location location = null;
        if (!lat.equalsIgnoreCase("") && !lng.equalsIgnoreCase("")) {
            location = new Location("");
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lng));
        }
        return location;
    }

    private static String getPreferences(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("COMMON_DB", 0);
        return sharedPreferences.getString(key, "");
    }

    private static void setPreferences(Context con, String key, String value) {
        SharedPreferences preferences = con.getSharedPreferences("COMMON_DB", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
