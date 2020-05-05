package com.nadu.foodsearch;

import androidx.annotation.NonNull;

public class GeoCoderGetAddress {
    private double x;
    private double y;
    private String addr;

    private boolean havePoint;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public boolean isHavePoint() {
        return havePoint;
    }

    public void setHavePoint(boolean havePoint) {
        this.havePoint = havePoint;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("x : ");
        builder.append(x);
        builder.append("y : ");
        builder.append(y);
        builder.append("addr : ");
        builder.append(addr);


        return builder.toString();
    }
}
