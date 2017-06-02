package com.onebit.wjluk.beaconoid;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.JsonConverter;
import com.onebit.wjluk.beaconoid.util.SqlHelper;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;


public class Dash2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer, RangeNotifier,
        GoogleApiClient.OnConnectionFailedListener{

    private FragmentManager fragmentManager;
    private BeaconManager mBeaconManager;
    private Identifier nSpace = null;
    private Identifier ins = null;
    private static final int PERMISSION_LOCATION = 1001;
    public static final String TAG = Dash2Activity.class.getSimpleName();
    private String bID;
    protected ArrayList<Ad> adslist;
    private AdFragment adFragment;
    private AdManager manager;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION);

            }
        }
        manager = AdManager.getInstance();
        adslist = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchTask task = new FetchTask();
                bID = "UR_0020";
                manager.setbId(bID);
                task.execute(AdManager.getInstance().getEmail(),bID);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        adFragment = new AdFragment();
        fragmentTransaction.add(R.id.dash_container,adFragment);
        fragmentTransaction.commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_sign_out){
            signout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signout(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        // [END_EXCLUDE]
                    }
                });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stream) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           if(adFragment == null) {
               adFragment = new AdFragment();
           }
            fragmentTransaction.replace(R.id.dash_container,adFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_fav) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FavFragment favFragment = new FavFragment();
            fragmentTransaction.replace(R.id.dash_container,favFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mBeaconManager =
                            BeaconManager.getInstanceForApplication(this.getApplicationContext());
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
                    bID = nSpace.toString()+ins.toString();
                    manager.setDistance(beacon.getDistance());
                    fetch();
                } else if (nSpace.compareTo(namespaceId) != 0 || ins.compareTo(instanceId)!=0){
                    nSpace = namespaceId;
                    ins = instanceId;
                    bID = nSpace.toString()+ins.toString();
                    fetch();
                }


            }
        }
    }

    private void fetch() {
        FetchTask task = new FetchTask();
        manager.setbId(bID);
        task.execute(manager.getEmail(),bID);
    }

    public void setAdFrag() {
        Fragment f = fragmentManager.findFragmentById(R.id.dash_container);
        if(f != null && f instanceof AdFragment){
            AdFragment adFragment = (AdFragment) f;
            Log.d("setAdFrag","setAdFrag called");
            adFragment.setAd();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class FetchTask extends AsyncTask<String,Void,Void> {
        private boolean needSet = false;

        @Override
        protected Void doInBackground(String... params) {
            ArrayList<Ad> aList = new ArrayList<>();
            String bID = params[1];
            String email = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;
            try {
                AdManager manager = AdManager.getInstance();
                double dis = manager.getDistance();
                String phone = manager.getPhone();
                final String AD_BASE_URL = "https://api.beaconoid.me";
                Uri builtUri = Uri.parse(AD_BASE_URL).buildUpon()
                        .appendPath("api")
                        .appendPath("v1")
                        .appendPath("advertisements")
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("beacon_id", bID)
                        .appendQueryParameter("distance",dis+"")
                        .appendQueryParameter("phone",phone)
                        .build();
                URL url = new URL(builtUri.toString());
                //Log.d(TAG,url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonString = buffer.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG,"MalformedURLException");

            } catch (IOException e) {
                Log.e(TAG,"IOException");

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG,"IOException");

                    }
                }
            }
            if(jsonString != null) {
                if(!jsonString.contains("failed")){
                    aList.addAll(JsonConverter.convert(jsonString));
                    AdManager manager = AdManager.getInstance();
                    manager.setAds(aList);
                    manager.setbId(bID);

                    Log.d("json", aList.size()+"");
                    needSet = true;
                    SqlHelper helper = new SqlHelper(getApplicationContext());
                    for (int i=0; i<aList.size(); i++) {
                        Ad ad = aList.get(i);
                        ContentValues values = new ContentValues();
                        values.put(SqlHelper.COLUMN_AD_ID,ad.getId());
                        values.put(SqlHelper.COLUMN_AD_NAME,ad.getName());
                        values.put(SqlHelper.COLUMN_BID,ad.getbId());
                        values.put(SqlHelper.COLUMN_CID,ad.getcId());
                        values.put(SqlHelper.COLUMN_PRICE,ad.getPrice());
                        values.put(SqlHelper.COLUMN_DES,ad.getDescription());
                        values.put(SqlHelper.COLUMN_URL, ad.getUrl());
                        values.put(SqlHelper.COLUMN_EXP,ad.getExpire());
                        values.put(SqlHelper.COLUMN_UPDATE, ad.getUpdate());
                        values.put(SqlHelper.COLUMN_BEACON,bID);
                        values.put(SqlHelper.COLUMN_LIKED,0);
                        helper.insert(SqlHelper.TABLE_ADS,values);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void ads) {
            if(needSet) {
                setAdFrag();
                needSet = false;
            }
        }
    }

}
