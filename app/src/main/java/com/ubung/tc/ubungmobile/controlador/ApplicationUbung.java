package com.ubung.tc.ubungmobile.controlador;

import android.app.Application;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.modelo.Singleton;

/**
 * Created by Tizon on 30/03/2015.
 */
public class ApplicationUbung extends Application {

    public void onCreate() {
        super.onCreate();

        Singleton singleton = Singleton.getInstance();
        try {
            singleton.inicializar(this.getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
