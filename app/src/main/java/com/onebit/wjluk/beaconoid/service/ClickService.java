package com.onebit.wjluk.beaconoid.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ClickService extends IntentService {

    public ClickService() {
        super("ClickService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            HttpURLConnection urlConnection = null;
            Ad ad = AdManager.getInstance().getAds().get(intent.getIntExtra("pos",-1));
            String aID = ad.getId()+"";
            String email = AdManager.getInstance().getEmail();
            try{
                final String AD_BASE_URL = "https://api.beaconoid.me";
                Uri builtUri = Uri.parse(AD_BASE_URL).buildUpon()
                        .appendPath("api")
                        .appendPath("v1")
                        .appendPath("advertisements")
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("advertisement_id", aID)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
                Log.e("error","IOException");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

}
