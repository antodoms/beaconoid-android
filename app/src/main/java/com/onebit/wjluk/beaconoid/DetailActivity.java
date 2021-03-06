package com.onebit.wjluk.beaconoid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.service.ClickService;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;
    private Ad ad;
    ImageView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        int pos = getIntent().getIntExtra("pos",-1);
        ArrayList<Ad> adsList = AdManager.getInstance().getAds();
        if(adsList != null & adsList.size() != 0){
            ad = AdManager.getInstance().getAds().get(pos);
            Intent intent = new Intent(this, ClickService.class);
            intent.putExtra("pos",pos);
            startService(intent);
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/OpenSans-ExtraBoldItalic.ttf");
            TextView name = (TextView) findViewById(R.id.tv_name);
            TextView des = (TextView) findViewById(R.id.tv_des);
            TextView price = (TextView) findViewById(R.id.tv_price);
            name.setTypeface(custom_font);
            name.setText(ad.getName());
            des.setTypeface(custom_font);
            des.setText(ad.getDescription());
            price.setTypeface(custom_font);
            price.setText("$"+ad.getPrice()+"");
            price.setBackgroundColor(getResources().getColor(R.color.blue));


            detail = (ImageView) findViewById(R.id.img_detail);
            Bitmap map = ad.getBitmap();
            if(map != null) {
                detail.setImageBitmap(map);
            } else {
                detail.setImageResource(R.drawable.placeholder);
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqlHelper helper = new SqlHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(SqlHelper.COLUMN_AD_ID,ad.getId());
                values.put(SqlHelper.COLUMN_AD_NAME,ad.getName());
                values.put(SqlHelper.COLUMN_BID,ad.getbId());
                values.put(SqlHelper.COLUMN_CID,ad.getcId());
                values.put(SqlHelper.COLUMN_PRICE,ad.getPrice());
                values.put(SqlHelper.COLUMN_DES,ad.getDescription());
                values.put(SqlHelper.COLUMN_URL, ad.getUrl());
                values.put(SqlHelper.COLUMN_EXP,ad.getExpire());
                values.put(SqlHelper.COLUMN_LIKED,1);
                Cursor c = db.query(SqlHelper.TABLE_ADS,
                        null,
                        SqlHelper.COLUMN_LIKED+"= ?" + "and "+SqlHelper.COLUMN_AD_ID + "=?",
                        new String[]{"1",ad.getId()+""},
                        null,
                        null,
                        null
                        );
                if(c == null || !c.moveToFirst()) {
                    db.update(SqlHelper.TABLE_ADS,values,SqlHelper.COLUMN_AD_ID+"="+ad.getId(),null);
                    Snackbar.make(view, R.string.action_save, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, R.string.action_already_saved, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                db.close();

            }
        });
    }


}
