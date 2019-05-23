package com.kv.swiggyaddress.util;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.EditText;

public  class Utils {
    public static String filterAddress(UserData.Address address) {
        String addresstxt = "";
        if (!address.getHouse_no().equalsIgnoreCase("")) {
            addresstxt += address.getHouse_no() + ", ";
        }
        if (!address.getLandmark().equalsIgnoreCase("")) {
            addresstxt += address.getLandmark() + ", ";
        }
        if (!address.getAddress().equalsIgnoreCase("")) {
            addresstxt += address.getAddress();

        }
        return addresstxt;
    }

    public static boolean isETEmpty(EditText et) {
        if (et.getText().toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
