package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.Threads.CameraAR;

public class AugmentedReality_Activity extends Activity {

    private CameraAR cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_reality_);
        initCamera();
    }

    private void initCamera() {
        if(cam==null) {
            try {
            FrameLayout prevt = (FrameLayout) findViewById(R.id.videoview);
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


}
