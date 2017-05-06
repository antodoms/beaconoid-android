package com.onebit.wjluk.beaconoid;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onebit.wjluk.beaconoid.model.Ad;
import com.onebit.wjluk.beaconoid.util.AdAdapter;
import com.onebit.wjluk.beaconoid.util.AdManager;
import com.onebit.wjluk.beaconoid.util.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdFragment extends Fragment {
    private final String TAG = AdFragment.class.getSimpleName();
    private ArrayList<Ad> adslist;
    private RecyclerView adList;
    private GridLayoutManager adLayoutManager;
    private AdAdapter adAdapter;


    public AdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adslist = new ArrayList<>();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ad, container, false);
        adList = (RecyclerView) v.findViewById(R.id.frag_ad_list);
        adLayoutManager = new GridLayoutManager(getActivity(),1);
        adAdapter = new AdAdapter(getActivity(),adslist);
        adList.setLayoutManager(adLayoutManager);
        adList.setAdapter(adAdapter);
        FetchTask fetchTask=new FetchTask();
        fetchTask.execute("czlukuan@gmail.com","UR_0020");
        return v;
    }

    public class FetchTask extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            String bID = params[1];
            String email = params[0];
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
            AdManager.getInstance().setAds(JsonConverter.convert(jsonString));
            adslist.addAll(AdManager.getInstance().getAds());
            adslist.addAll(AdManager.getInstance().getAds());
            adslist.addAll(AdManager.getInstance().getAds());
            adslist.addAll(AdManager.getInstance().getAds());
            Log.d(TAG, adslist.size()+"");
            return null;
        }

        @Override
        protected void onPostExecute(Void ads) {
            adAdapter.notifyDataSetChanged();
        }
    }


}
