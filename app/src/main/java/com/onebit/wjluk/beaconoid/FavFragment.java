package com.onebit.wjluk.beaconoid;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdAdapter;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {


    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Ad> adslist = new ArrayList<>();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        RecyclerView favList = (RecyclerView) v.findViewById(R.id.frag_fav_list);
        SqlHelper helper = new SqlHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SqlHelper.TABLE_ADS,
                null,
                null,
                null,
                null,
                null,
                null);
        while(cursor.moveToNext()) {
            long exp = cursor.getLong(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_EXP));
            if(System.currentTimeMillis() <= exp){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_NAME));
                int bid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_BID));
                int cid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_CID));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_PRICE));
                String des = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_DES));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_URL));
                Ad ad = new Ad(id,name,bid,cid,price,des,url,exp);
                adslist.add(ad);
            } else {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_ID));
                db.delete(SqlHelper.TABLE_ADS,SqlHelper.COLUMN_AD_ID+" =? ",new String[]{id+""});
            }
        }
        GridLayoutManager favLayoutManager = new GridLayoutManager(getActivity(), 1);
        AdAdapter adAdapter = new AdAdapter(getActivity(), adslist);
        favList.setLayoutManager(favLayoutManager);
        favList.setAdapter(adAdapter);
        return v;
    }

}
