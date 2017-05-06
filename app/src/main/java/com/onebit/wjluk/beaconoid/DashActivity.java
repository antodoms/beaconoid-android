package com.onebit.wjluk.beaconoid;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.service.FetchService;
import com.onebit.wjluk.beaconoid.util.AdAdapter;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.JsonConverter;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class DashActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    private static final String TAG = DashActivity.class.getSimpleName();
    private static final int PERMISSION_LOCATION = 1001;
    private String email;
    private ListView adsList;
    private AdAdapter adAdapter;
    private BeaconManager mBeaconManager;
    private Identifier nSpace = null;
    private Identifier ins = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = getIntent().getStringExtra("email");

        adsList = (ListView) findViewById(R.id.adList);
        adAdapter = new AdAdapter(this, new ArrayList<Ad>());
        //adsList.setAdapter(adAdapter);
        adsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("pos",position);
                startActivity(intent);

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION);

            }
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("FETCHED_JSON"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
// Detect the main identifier (UID) frame:
                    mBeaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
// Detect the telemetry (TLM) frame:
                    mBeaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
// Detect the URL frame:
                    mBeaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry Eddystone-TLM frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        mBeaconManager.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("json");
            Toast.makeText(getApplicationContext(), "received string length " + name.length(), Toast.LENGTH_SHORT).show();
            ArrayList<Ad> newList = JsonConverter.convert(name);
            AdManager.getInstance().setAds(newList);
            //adAdapter.clear();
            //adAdapter.addAll(newList);

        }
    };

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon : beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                if(nSpace == null || ins == null){
                    nSpace = namespaceId;
                    ins = instanceId;
                    fetch();
                } else if (nSpace.compareTo(namespaceId) != 0 || ins.compareTo(instanceId)!=0){
                    nSpace = namespaceId;
                    ins = instanceId;
                    fetch();
                }


            }
        }
    }

    private void fetch(){
        Intent intent = new Intent(this, FetchService.class);
        intent.putExtra("email",email);
        intent.putExtra("bID","UR_0020");
        startService(intent);
        Log.d(TAG,"found beacon"+nSpace+" "+ins);
    }
}