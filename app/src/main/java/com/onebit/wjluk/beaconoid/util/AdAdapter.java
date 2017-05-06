package com.onebit.wjluk.beaconoid.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.onebit.wjluk.beaconoid.R;
import com.onebit.wjluk.beaconoid.model.Ad;

import java.util.ArrayList;

/**
 * Created by jason on 4/05/17.
 */

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> {
    private Context mContext;
    private static final String TAG = AdAdapter.class.getSimpleName();
    private ArrayList<Ad> ads;
    public AdAdapter(@NonNull Context context, ArrayList<Ad> objs) {
        mContext = context;
        ads = objs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_item,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tmp.setText(ads.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView tmp;

        public ViewHolder(View itemView) {
            super(itemView);
            tmp = (TextView) itemView.findViewById(R.id.tv_ad_item);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getItemId() + " ");
        }
    }

}
