package com.onebit.wjluk.beaconoid.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onebit.wjluk.beaconoid.DetailActivity;
import com.onebit.wjluk.beaconoid.R;
import com.onebit.wjluk.beaconoid.model.Ad;

import java.util.ArrayList;

/**
 * Created by jason on 7/05/17.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private Context mContext;
    private static final String TAG = FavAdapter.class.getSimpleName();
    private ArrayList<Ad> ads;

    public FavAdapter(@NonNull Context context, ArrayList<Ad> objs) {
        mContext = context;
        ads = objs;
    }

    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_item,parent,false);
        FavAdapter.ViewHolder holder = new FavAdapter.ViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder (FavAdapter.ViewHolder holder, int position) {
        holder.tmp.setText(ads.get(position).getName());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
//                    Intent intent = new Intent(mContext, DetailActivity.class);
//                    intent.putExtra("pos",position);
//                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private TextView tmp;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            tmp = (TextView) itemView.findViewById(R.id.tv_ad_item);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }
        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
