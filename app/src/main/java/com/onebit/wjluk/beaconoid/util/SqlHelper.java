package com.onebit.wjluk.beaconoid.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by jason on 11/05/17.
 */

public class SqlHelper extends SQLiteOpenHelper {
    private Context mContext;

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
    public static final String COLUMN_UPDATE = "last_update";
    public static final String COLUMN_BEACON = "beacon_id";
    public static final String COLUMN_LIKED = "liked";

    private static final int version =5;

    private static final String DATABASE_CREATE_ADS = "create table "
            + TABLE_ADS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AD_ID + " REAL NOT NULL, "
            + COLUMN_AD_NAME + " TEXT NOT NULL, "
            + COLUMN_BID + " REAL NOT NULL, "
            + COLUMN_CID + " REAL NOT NULL, "
            + COLUMN_PRICE + " REAL NOT NULL, "
            + COLUMN_DES + " TEXT NOT NULL, "
            + COLUMN_URL + " TEXT NOT NULL, "
            + COLUMN_EXP + " REAL, "
            + COLUMN_UPDATE + " TEXT NOT NULL, "
            + COLUMN_BEACON + " REAL NOT NULL, "
            + COLUMN_LIKED + " INTEGER NOT NULL "
            +");";


    public SqlHelper(Context context ) {
        super(context, DATABASE_NAME, null, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_ADS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);
        onCreate(db);
    }

    public void insert(String table, ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(SqlHelper.TABLE_ADS,
                null,
                SqlHelper.COLUMN_AD_ID+"= ?",
                new String[]{values.getAsString(SqlHelper.COLUMN_AD_ID)},
                null,
                null,
                null
        );
        if(c == null || !c.moveToFirst()) {
            db.insert(table,null,values);
        } else {
            db.update(SqlHelper.TABLE_ADS,values,SqlHelper.COLUMN_AD_ID+"= ?",
                    new String[]{values.getAsString(SqlHelper.COLUMN_AD_ID)});
        }
    }
}
