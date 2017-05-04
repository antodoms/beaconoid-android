package com.onebit.wjluk.beaconoid.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class AdAdapter extends ArrayAdapter<Ad> {
    Context mContext;
    public AdAdapter(@NonNull Context context, ArrayList<Ad> objs) {
        super(context, 0,objs);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_ad_item);
        String dis = getItem(position).getName();
        tv.setText(dis);
        return convertView;
    }
}
