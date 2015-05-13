package com.ubung.tc.ubungmobile.controlador.Threads;

import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Tizon on 15/04/2015.
 */
public class AnimationAugmentedReality extends Thread {

    private final int duration;
    private final ImageView zona;
    private final FrameLayout frameView;
    private Handler h;
    private int positionx;
    private int positiony;

    public AnimationAugmentedReality(Handler m, ImageView zona, FrameLayout prevt) {
        this.h = m;
        this.zona=zona;
        this.frameView=prevt;
        duration = 1000;
        positionx = 0;
        positiony = 0;
    }

    public void run() {
        int i=0;
        while(i<duration) {
            try {
            RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) zona.getLayoutParams();
            par.leftMargin = positionx;
            par.topMargin = positiony;
            zona.setRotation(90);
            Message m = new Message();
            m.obj = par;
            h.sendMessage(m);
            i++;
            Thread.sleep(10);

            if(i==duration){
                i=0;
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setPosition(int x,int y) {
        this.positionx=x;
        this.positiony=y;
    }
}
