package com.ubung.tc.ubungmobile.controlador.Threads;

import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Tizon on 15/04/2015.
 */
public class AnimationZona extends Thread {

    private final int duration;
    private Handler h;

    public AnimationZona(Handler m){
        this.h=m;
        duration=1000;
    }

    public void run(){
        int i=0;
        int radio=0;
        while(i<duration){
            if(radio>150){
                radio=0;
            }
            Message m= new Message();

            m.obj=radio;
            h.sendMessage(m);
            radio++;
            i++;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message m= new Message();
        radio=-1;
        m.obj=radio;
        h.sendMessage(m);

    }
}
