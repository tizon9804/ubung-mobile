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


    private final ImageView zona;
    private final FrameLayout frameView;
    private Handler h;
    private int positionx;
    private int positiony;
    private int dist;

    public AnimationAugmentedReality(Handler m, ImageView zona, FrameLayout prevt) {
        this.h = m;
        this.zona=zona;
        this.frameView=prevt;

        positionx = 0;
        positiony = 0;
    }

    public void run() {
        while(true) {
            try {
            RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) zona.getLayoutParams();
            par.leftMargin = positionx;
            par.topMargin = positiony;
            Message m = new Message();
            m.obj = par;
            h.sendMessage(m);
            Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setPosition(int x,int y) {
        this.positionx=x;
        this.positiony=y;
        this.dist=0;
    }
}
