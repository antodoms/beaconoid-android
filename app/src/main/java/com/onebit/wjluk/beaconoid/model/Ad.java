package com.onebit.wjluk.beaconoid.model;

import android.graphics.Bitmap;

/**
 * Created by jason on 4/05/17.
 */

public class Ad {
    private int id,bId,cId;
    private String name,description,url;
    private double price;
    private long expire;
    private String update;
    Bitmap bitmap;
    public Ad() {

    }
    public Ad(int id,String name,int bId,int cId,double price, String description,
              String url, String update,long expire){
        this.id = id;
        this.name = name;
        this.bId = bId;
        this.cId = cId;
        this.price = price;
        this.description = description;
        this.url = url;
        this.update = update;
        this.expire = expire;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUpdate() {
        return update;
    }
}
