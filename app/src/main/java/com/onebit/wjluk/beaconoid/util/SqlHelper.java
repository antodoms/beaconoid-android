package com.onebit.wjluk.beaconoid.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jason on 11/05/17.
 */

public class SqlHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ads.db";
    public static final String TABLE_ADS = "ads";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_AD_ID = "ad_id";
    public static final String COLUMN_AD_NAME = "name";
    public static final String COLUMN_BID = "bid";
    public static final String COLUMN_CID = "cid";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DES = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_EXP = "expire";
    private static final int version =2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ADS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AD_ID + " REAL NOT NULL, "
            + COLUMN_AD_NAME + " TEXT NOT NULL, "
            + COLUMN_BID + " REAL NOT NULL, "
            + COLUMN_CID + " REAL NOT NULL, "
            + COLUMN_PRICE + " REAL NOT NULL, "
            + COLUMN_DES + " TEXT NOT NULL, "
            + COLUMN_URL + " TEXT NOT NULL, "
            + COLUMN_EXP + " REAL NOT NULL"
            +");";

    public SqlHelper(Context context ) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);
        onCreate(db);
    }
}
