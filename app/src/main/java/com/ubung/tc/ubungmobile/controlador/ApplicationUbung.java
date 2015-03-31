package com.ubung.tc.ubungmobile.controlador;

import android.app.Application;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.modelo.Singleton;

/**
 * Created by Tizon on 30/03/2015.
 */
public class ApplicationUbung extends Application {

    public void onCreate() {
    super.onCreate();

        Singleton singleton = Singleton.getInstance();
        singleton.inicializar(this.getApplicationContext());

    }

}
