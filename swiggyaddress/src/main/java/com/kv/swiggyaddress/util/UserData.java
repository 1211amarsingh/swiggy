package com.kv.swiggyaddress.util;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {

    ArrayList<Address> address;

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public class Address implements Serializable {

        private String id;
        private String type;
        private String house_no;
        private String landmark;
        private String address;
        private double lat;
        private double lng;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHouse_no() {
            return house_no;
        }

        public void setHouse_no(String house_no) {
            this.house_no = house_no;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }
    }
}
