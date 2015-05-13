package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.Threads.AnimationAugmentedReality;
import com.ubung.tc.ubungmobile.controlador.Threads.CameraAR;

import java.io.IOException;

public class AugmentedReality_Activity extends Activity {

    private CameraAR cam;
    private FrameLayout prevt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_reality_);
        initCamera();
        initZonaRendered();
    }

    private void initZonaRendered() {

        final ImageView zona=(ImageView) findViewById(R.id.zonaViewRendered);
        Handler handposition=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                RelativeLayout.LayoutParams r=(RelativeLayout.LayoutParams)msg.obj;
                zona.setLayoutParams(r);
                return true;
            }
        });

        AnimationAugmentedReality ar= new AnimationAugmentedReality(handposition,zona,prevt);
        ar.start();



    }

    private void initCamera() {
        if(cam==null) {
            try {
            prevt = (FrameLayout) findViewById(R.id.videoview);
            cam = new CameraAR(this.getApplicationContext(), prevt);
                while(cam.getmPreview()==null) {/*espera a que inicialice*/}
               prevt.addView(cam.getmPreview());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void onDestroy(){
        super.onDestroy();



    }

    public void onPause(){
        super.onPause();
        Camera mCamera=cam.getmCamera();
        try {
           // mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            cam.getmPreview().getHolder().removeCallback(cam.getmPreview());
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
