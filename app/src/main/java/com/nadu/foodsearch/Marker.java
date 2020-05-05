package com.nadu.foodsearch;

public class Marker {
    private double lat;
    private double lng;
    private String shop_Name;
    private String shop_Food;

    public String getShop_Name() {
        return shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        this.shop_Name = shop_Name;
    }

    public String getShop_Food() {
        return shop_Food;
    }

    public void setShop_Food(String shop_Food) {
        this.shop_Food = shop_Food;
    }

    public Marker() {}

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
