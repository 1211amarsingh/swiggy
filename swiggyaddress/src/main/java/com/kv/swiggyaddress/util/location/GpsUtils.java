package com.kv.swiggyaddress.util.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.kv.swiggyaddress.util.Constant;
import com.kv.swiggyaddress.util.Log;

import static android.content.Context.LOCATION_SERVICE;


public class GpsUtils {
    private Context context;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    public GpsUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
    }

    public static boolean isGpsOn(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // method for turn on GPS
    public void turnGPSOn(onGpsListener onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((Activity) context, locationSettingsResponse -> {
                        //  GPS is already enable, callback GPS status through listener
                        if (onGpsListener != null) {
                            onGpsListener.gpsStatus(true);
                        }
                    })
                    .addOnFailureListener((Activity) context, e -> {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) context, Constant.GPS_REQUEST);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.loge("PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.loge("Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings.");
                        }
                    });
        }
    }

    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
}