package com.ubung.tc.ubungmobile.controlador.Threads;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Tizon on 15/04/2015.
 */
public class AnimationZona extends Thread {

    private final int duration;
    private Handler h;

    public AnimationZona(Handler m) {
        this.h = m;
        duration = 100;
    }

    public void run() {
        int i = 0;
        int radio = 0;
        while (i < duration) {
            if (radio > 100) {
                radio = 0;
            }
            Message m = new Message();

            m.obj = radio;
            h.sendMessage(m);
            radio++;
            i++;
            try {
                int effect=radio;
                if(radio<5){
                    effect=5;
                }
                else if(radio>20){
                    effect=20;
                }
                Thread.sleep(effect);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message m = new Message();
        radio = -1;
        m.obj = radio;
        h.sendMessage(m);

    }
}
