package com.onebit.wjluk.beaconoid.service;

import android.app.IntentService;
import android.content.Intent;


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
        }
    }

}
