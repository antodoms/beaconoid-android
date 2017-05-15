package com.onebit.wjluk.beaconoid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.service.ClickService;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;
    private Ad ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        int pos = getIntent().getIntExtra("pos",-1);
        ad = AdManager.getInstance().getAds().get(pos);
        Intent intent = new Intent(this, ClickService.class);
        intent.putExtra("pos",pos);
        startService(intent);

        TextView tmp = (TextView) findViewById(R.id.tv_tmp);
        tmp.setText(ad.getName()+" "+ad.getDescription()+" "+ad.getPrice());

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
                db.insert(SqlHelper.TABLE_ADS,null,values);

                Snackbar.make(view, R.string.action_save, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
