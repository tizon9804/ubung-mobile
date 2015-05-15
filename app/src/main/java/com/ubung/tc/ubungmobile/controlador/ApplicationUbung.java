package com.ubung.tc.ubungmobile.controlador;

import android.app.Application;
import android.os.Looper;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

/**
 * Created by Tizon on 30/03/2015.
 */
public class ApplicationUbung extends Application {
    public static boolean active=true;
    public void onCreate() {
        super.onCreate();

        final Singleton singleton = Singleton.getInstance();
        final ApplicationUbung con = this;
        try {
            Thread t= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        singleton.inicializar(con.getApplicationContext());
                        active=false;

                    } catch (ParseException e) {
                        active=false;
                        e.printStackTrace();
                    }
                }
            });
          t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
