package com.ubung.tc.ubungmobile.controlador.Threads;
import android.content.Context;
import android.hardware.Camera;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;

import com.ubung.tc.ubungmobile.controlador.adapters.CameraPreviewAdapter;


/**
 * Created by Tizon on 07/05/2015.
 */
public class CameraAR extends Thread {



    private Context context;
    private FrameLayout prevt;
    private Camera mCamera;



    private CameraPreviewAdapter mPreview;


    public CameraAR(Context context, FrameLayout prevt){
        this.context=context;
        this.prevt=prevt;
        start();
    }

    @Override
    public void run(){
        Looper.prepare();
        mPreview=new CameraPreviewAdapter(context,this);
    }

    public Camera safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e("Robocamera", "failed to open Camera");
            e.printStackTrace();
            return null;
        }

        return mCamera;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    public Camera getmCamera() {
        return mCamera;
    }
    public CameraPreviewAdapter getmPreview() {
        return mPreview;
    }




}
