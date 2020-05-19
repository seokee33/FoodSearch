package com.nadu.foodsearch;

import java.io.Serializable;

public class FoodList implements Serializable {
    private String shopname;
    private String shopfood;
    private String shoptime;
    private String shopprofile;

    private String shopnumber;
    private double tv_Shop_Lat;
    private double tv_Shop_Lng;

    private String id;

    public FoodList() {}

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopfood() {
        return shopfood;
    }

    public void setShopfood(String shopfood) {
        this.shopfood = shopfood;
    }

    public String getShoptime() {
        return shoptime;
    }

    public void setShoptime(String shoptime) {
        this.shoptime = shoptime;
    }

    public String getShopprofile() {
        return shopprofile;
    }

    public void setShopprofile(String shopprofile) {
        this.shopprofile = shopprofile;
    }

    public String getShopnumber() {
        return shopnumber;
    }

    public void setShopnumber(String shopnumber) {
        this.shopnumber = shopnumber;
    }

    public double getTv_Shop_Lat() {
        return tv_Shop_Lat;
    }

    public void setTv_Shop_Lat(double tv_Shop_Lat) {
        this.tv_Shop_Lat = tv_Shop_Lat;
    }

    public double getTv_Shop_Lng() {
        return tv_Shop_Lng;
    }

    public void setTv_Shop_Lng(double tv_Shop_Lng) {
        this.tv_Shop_Lng = tv_Shop_Lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
