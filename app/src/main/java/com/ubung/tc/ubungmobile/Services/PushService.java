package com.ubung.tc.ubungmobile.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ubung.tc.ubungmobile.modelo.Singleton;

public class PushService extends IntentService {



    public PushService() {
        super("pushService");
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(android.content.Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Singleton singleton = Singleton.getInstance();
        singleton.inicializar(this.getApplicationContext());
    }
}
