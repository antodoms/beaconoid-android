package com.onebit.wjluk.beaconoid.util;

import android.util.Log;

import com.onebit.wjluk.beaconoid.model.Ad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jason on 4/05/17.
 */

public class JsonConverter {
    private static final String TAG = JsonConverter.class.getSimpleName();
    public static ArrayList<Ad> convert(String str) {
        ArrayList<Ad> ads = new ArrayList<>();
        //check if return the ad successfully. length < 50 means only return the error code
        if(str.length()>50){
            try{
                JSONObject top = new JSONObject(str);
                JSONArray adArray = top.getJSONArray("advertisements");
                for(int i=0;i<adArray.length();i++){
                    JSONObject ad = adArray.getJSONObject(i);
                    int id = ad.getInt("id");
                    String name = ad.getString("name");
                    int bID = ad.getInt("beacon_id");
                    int cID = ad.getInt("category_id");
                    double price = Double.parseDouble(ad.getString("category_id"));
                    String des = ad.getString("category_id");
                    String url = ad.getString("image");
                    Ad adv = new Ad(id,name,bID,cID,price,des,
                            url,System.currentTimeMillis()+7200000);
                    ads.add(adv);
                }
                int i=0;
                return ads;
            } catch (JSONException e){
                Log.d(TAG,"invalid json!");
                return  new ArrayList<>();
            }
        }
        return ads;
    }
}
