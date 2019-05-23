package com.kv.swiggyaddress.util.location;

import android.location.Location;

public class LocationInstance {
    private static LocationInstance mInstance;
    private LocationListener mListener;

    private LocationInstance() {
    }

    public static LocationInstance getInstance() {
        if (mInstance == null) {
            mInstance = new LocationInstance();
        }
        return mInstance;
    }

    public void setListener(LocationListener listener) {
        mListener = listener;
    }

    void changeState(Location location) {
        if (mListener != null) {
            mListener.onLocationChange(location);
        }
    }
}
