package com.kv.swiggyaddress.util;


public class Log {
    private static final boolean DEVELOPER_MODE = true;
    private static String Tag = "SwiggyAddress";

    public static void log(String tag, String message) {
        android.util.Log.w(tag, message);
    }


    public static void logw(String message) {
        if (DEVELOPER_MODE)
            android.util.Log.w(Tag, message);
    }

    public static void loge(String message) {
        if (DEVELOPER_MODE)
            android.util.Log.e(Tag, message);
    }
    public static void logd(String message) {
        if (DEVELOPER_MODE)
            android.util.Log.d(Tag, message);
    }
}
