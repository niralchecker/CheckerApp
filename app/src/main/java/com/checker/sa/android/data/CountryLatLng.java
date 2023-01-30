package com.checker.sa.android.data;

public class CountryLatLng {
    private String country;
    private double lat,lng;

    public CountryLatLng(String country, String lat, String lng) {
        this.country = country;
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
}
