package com.onebit.wjluk.beaconoid;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdAdapter;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.JsonConverter;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdFragment extends Fragment {
    private final String TAG = AdFragment.class.getSimpleName();
    private ArrayList<Ad> adslist;
    private RecyclerView adList;
    private GridLayoutManager adLayoutManager;
    private AdAdapter adAdapter;
    private  AdManager manager;


    public AdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("lifecycle","adfrag oncreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("lifecycle","adfrag onActivityCreated");
        setAd();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("lifecycle","adfrag onCreateView");
        View v = inflater.inflate(R.layout.fragment_ad, container, false);
        adslist = new ArrayList<>();
        manager = AdManager.getInstance();

        adList = (RecyclerView) v.findViewById(R.id.frag_ad_list);
        adLayoutManager = new GridLayoutManager(getActivity(),1);
        adAdapter = new AdAdapter(getActivity(),adslist);
        adList.setLayoutManager(adLayoutManager);
        adList.setAdapter(adAdapter);
        return v;
    }


    public void setAd(){
        dbQuery();

        Log.d(TAG,"about to change dataset");
    }

    private void dbQuery() {
        adslist.clear();
        ArrayList<Ad> returnList = new ArrayList<>();
        manager = AdManager.getInstance();
        SqlHelper helper = new SqlHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String test = manager.getbId();
        Cursor cursor = db.query(SqlHelper.TABLE_ADS,
                null,
                SqlHelper.COLUMN_BEACON + " = ?",
                new String[]{test},
                null,
                null,
                null);
        while (cursor.moveToNext()){
            long exp = cursor.getLong(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_EXP));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_NAME));
            int bid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_BID));
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_CID));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_PRICE));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_DES));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_URL));
            String update = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_UPDATE));
            Ad ad = new Ad(id,name,bid,cid,price,des,url,update,exp);
            returnList.add(ad);

        }
        adslist.addAll(returnList);
        adAdapter.notifyDataSetChanged();
    }



}
