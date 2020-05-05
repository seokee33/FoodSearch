package com.nadu.foodsearch;

import androidx.multidex.MultiDexApplication;

public class CustomApplication extends MultiDexApplication {
    private static CustomApplication instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }
}