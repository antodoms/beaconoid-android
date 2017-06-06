package com.onebit.wjluk.beaconoid;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.onebit.wjluk.beaconoid.util.FavAdapter;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {

    private ArrayList<Ad> adslist;
    private FavAdapter favAdapter;


    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle","favfrag onCreateView");
        adslist = new ArrayList<>();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        RecyclerView favList = (RecyclerView) v.findViewById(R.id.frag_fav_list);
        GridLayoutManager favLayoutManager = new GridLayoutManager(getActivity(), 1);
        favAdapter = new FavAdapter(getActivity(), adslist);
        favList.setLayoutManager(favLayoutManager);
        favList.setAdapter(favAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SqlHelper helper = new SqlHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SqlHelper.TABLE_ADS,
                null,
                null,
                null,
                null,
                null,
                null);
        adslist.clear();
        while(cursor.moveToNext()) {

            long exp = cursor.getLong(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_EXP));
            int liked = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_LIKED));
            if(System.currentTimeMillis() <= exp && liked == 1){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_NAME));
                int bid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_BID));
                int cid = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_CID));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_PRICE));
                String des = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_DES));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_URL));
                String update = cursor.getString(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_UPDATE));
                Ad ad = new Ad(id,name,bid,cid,price,des,url,update,exp);
                adslist.add(ad);

            } else {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlHelper.COLUMN_AD_ID));
                ContentValues values = new ContentValues();
                values.put(SqlHelper.COLUMN_LIKED,0);
                db.update(SqlHelper.TABLE_ADS,values,SqlHelper.COLUMN_AD_ID+"="+id,null);
            }
        }
        favAdapter.notifyDataSetChanged();
    }
}
