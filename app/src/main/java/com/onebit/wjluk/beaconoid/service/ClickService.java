package com.onebit.wjluk.beaconoid.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.onebit.wjluk.beaconoid.R;
import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        BufferedReader reader = null;
        String jsonString = null;
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
                JSONObject top = new JSONObject(jsonString);
                String status = top.getString("status");
                Toast.makeText(getApplicationContext(), status,Toast.LENGTH_SHORT).show();


            }  catch (IOException er) {
                er.printStackTrace();
                Log.e("error","IOException");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

}
