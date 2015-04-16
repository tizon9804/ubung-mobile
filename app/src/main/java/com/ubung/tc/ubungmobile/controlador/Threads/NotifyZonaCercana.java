package com.ubung.tc.ubungmobile.controlador.Threads;

import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

/**
 * Created by Tizon on 15/04/2015.
 */
public class NotifyZonaCercana extends Thread {

    private Location map;
    private ArrayList<Zona> zs;
    private Handler hand;

    public NotifyZonaCercana(Location map, Handler h, ArrayList<Zona> zs) {
        this.hand = h;
        this.map = map;
        this.zs = zs;
    }

    public void run() {
        Message m = new Message();


        for (Zona z : zs) {
            LatLng zonapos = new LatLng(z.getLatLongZoom()[0], z.getLatLongZoom()[1]);

            LatLng loc = new LatLng(map.getLatitude(), map.getLongitude());
            if (cercaZona(loc, zonapos)) {
                m.obj = z;
                hand.sendMessage(m);
                this.interrupt();
            }
        }

    }


    public boolean cercaZona(LatLng pos, LatLng zona) {

        double d = 0;
        double x = Math.pow((zona.latitude - pos.latitude), 2);
        double y = Math.pow(zona.longitude - pos.longitude, 2);
        d = Math.sqrt(x + y) * 100;
        d = d * 1000;
        // Log.e("calculo entro en zona", "La distancia es de:" + d);
        if (d < 200) {
            return true;
        }

        return false;
    }
}
