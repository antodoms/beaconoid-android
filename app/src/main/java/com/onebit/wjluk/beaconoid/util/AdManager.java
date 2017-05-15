package com.onebit.wjluk.beaconoid.util;

import com.onebit.wjluk.beaconoid.model.Ad;

import java.util.ArrayList;

/**
 * Created by jason on 4/05/17.
 */

public class AdManager {
    private ArrayList<Ad> ads;
    private static AdManager mInstance = null;
    private String email;

    public static AdManager getInstance(){
        if(mInstance == null)
        {
            mInstance = new AdManager();
        }
        return mInstance;
    }
    private AdManager(){
        ads = new ArrayList<>();
        email = "";
    }

    public void setAds(ArrayList<Ad> ads) {
        this.ads = null;
        this.ads = ads;
    }

    public ArrayList<Ad> getAds() {
        return ads;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
