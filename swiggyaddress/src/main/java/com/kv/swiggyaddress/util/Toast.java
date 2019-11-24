package com.kv.swiggyaddress.util;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

public class Toast {
    public static void showToast(Activity activity, String message) {
        android.widget.Toast.makeText(activity, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Activity activity, String message) {
        android.widget.Toast.makeText(activity, message, android.widget.Toast.LENGTH_LONG).show();
    }

    /*********************
     * show toast message
     ***********************/
    public static void showToast(Context context, String message) {
        android.widget.Toast.makeText(context, "" + message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(Activity view, String message) {

        Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
    public static void showSnackBar(View view, String message) {

        Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
