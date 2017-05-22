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
    private double distance =-1;

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

    public void setAds(ArrayList<Ad> adl) {
        ads = null;
        ads = adl;
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

    public void setDistance(double dis) {
        distance = dis;
    }

    public double getDistance() {
        return distance;
    }
}
