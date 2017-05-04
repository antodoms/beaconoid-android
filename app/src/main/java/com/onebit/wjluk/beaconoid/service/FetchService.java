package com.onebit.wjluk.beaconoid.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FetchService extends IntentService {
    private static final String TAG = FetchService.class.getSimpleName();
    public FetchService(){
        super("FetchService"); //coment
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String bID = intent.getStringExtra("bID");
            String email = intent.getStringExtra("email");
            if (bID != null && email != null) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String jsonString = null;
                try {
                    final String AD_BASE_URL = "https://api.beaconoid.me";
                    Uri builtUri = Uri.parse(AD_BASE_URL).buildUpon()
                            .appendPath("api")
                            .appendPath("v1")
                            .appendPath("advertisements")
                            .appendQueryParameter("email", email)
                            .appendQueryParameter("beacon_id", bID)
                            .build();
                    URL url = new URL(builtUri.toString());

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return;
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
                        return;
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
                Intent adJson = new Intent();
                adJson.setAction("FETCHED_JSON");
                adJson.putExtra("json", jsonString);
                LocalBroadcastManager.getInstance(this).sendBroadcast(adJson);
            }
        }
    }

}
