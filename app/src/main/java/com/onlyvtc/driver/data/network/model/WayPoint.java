package com.onlyvtc.driver.data.network.model;

import android.util.Log;

import org.json.JSONObject;

public class WayPoint {
    Double lng;
    Double lat;
    String formatted_address;
    String region_name;
    String country;
    String address;

    public WayPoint(String jsonData) {
        try {
            JSONObject object = new JSONObject(jsonData);
            lng = object.optDouble("lng", 0.0);
            lat = object.optDouble("lat", 0.0);
            formatted_address = object.optString("formatted_address");
            region_name = object.optString("region_name");
            country = object.optString("country");
            address = object.optString("address");
        } catch (Exception e) {
            Log.d("init way point", "WayPoint: " + e.toString());
        }
    }

    public WayPoint(JSONObject object) {
        lng = object.optDouble("lng", 0.0);
        lat = object.optDouble("lat", 0.0);
        formatted_address = object.optString("formatted_address");
        region_name = object.optString("region_name");
        country = object.optString("country");
        address = object.optString("address");
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
