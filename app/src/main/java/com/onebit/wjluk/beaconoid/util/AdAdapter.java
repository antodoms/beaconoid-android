package com.onebit.wjluk.beaconoid.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onebit.wjluk.beaconoid.DetailActivity;
import com.onebit.wjluk.beaconoid.R;
import com.onebit.wjluk.beaconoid.model.Ad;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        AdAdapter.ViewHolder holder = new ViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tmp.setText(ads.get(position).getName());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("pos",position);
                    mContext.startActivity(intent);
                }
            }
        });
        ArrayList<Ad> tmp = AdManager.getInstance().getAds();
        Bitmap img = null;
        if(tmp != null && tmp.size() != 0) {
            img = tmp.get(position).getBitmap();
        }
        if(img == null){
            String url = ads.get(position).getUrl();
            Picasso.with(mContext).load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ArrayList<Ad> checkNull = AdManager.getInstance().getAds();
                            if(checkNull != null&& checkNull.size() != 0) {
                                checkNull.get(position).setBitmap(bitmap);
                                holder.item.setImageBitmap(bitmap);
                            }

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            //holder.item.setImageDrawable(errorDrawable);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            holder.item.setImageDrawable(placeHolderDrawable);
                        }
                    });

        } else {
            holder.item.setImageBitmap(img);
        }
        //holder.item.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private TextView tmp;
        private ImageView item;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            tmp = (TextView) itemView.findViewById(R.id.tv_ad_item);
            item = (ImageView) itemView.findViewById(R.id.img_item);

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
