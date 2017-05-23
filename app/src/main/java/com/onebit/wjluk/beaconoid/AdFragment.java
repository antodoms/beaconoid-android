package com.onebit.wjluk.beaconoid;


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


    public AdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(adslist == null) {
            adslist = new ArrayList<>();
            adslist.addAll(AdManager.getInstance().getAds());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adslist = AdManager.getInstance().getAds();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ad, container, false);
        adList = (RecyclerView) v.findViewById(R.id.frag_ad_list);
        adLayoutManager = new GridLayoutManager(getActivity(),1);
        adAdapter = new AdAdapter(getActivity(),adslist);
        adList.setLayoutManager(adLayoutManager);
        adList.setAdapter(adAdapter);
        return v;
    }


    public void setAd(){
        adslist.clear();
        adslist.addAll(AdManager.getInstance().getAds());
        Log.d(TAG,"about to change dataset");
        adAdapter.notifyDataSetChanged();
    }



}
