package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ubung.tc.ubungmobile.controlador.Threads.CameraAR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Tizon on 07/05/2015.
 */
public class CameraPreviewAdapter extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG ="Camera Streaming" ;
    private CameraAR camerAR;
    private cam_PreviewCallback frame;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;

    public CameraPreviewAdapter(Context context, CameraAR cameraAR) {
        super(context);

        try {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.new
        mHolder = getHolder();
        mHolder.setSizeFromLayout();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.camerAR=cameraAR;
        setCamera(camerAR.safeCameraOpen(0));
        frame= new cam_PreviewCallback();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCamera(Camera camera) {
        try {
            if (mCamera == camera) {
                return;
            }

            //stopPreviewAndFreeCamera();

            mCamera = camera;

            if (mCamera != null) {
                List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
                pixels = new int[mCamera.getParameters().getPreviewSize().width * mCamera.getParameters().getPreviewSize().height];
                mSupportedPreviewSizes = localSizes;
                requestLayout();
                try {
                    mCamera.setPreviewCallback(frame);
                    mCamera.setPreviewDisplay(mHolder);

                } catch (IOException e) {
                    Log.d("surface", "catch Preview..." + e.getMessage());
                }

                // Important: Call startPreview() to start updating the preview
                // surface. Preview must be started before you can take a picture.
                Log.d("surface", "Starting Preview camera... ");

                mCamera.startPreview();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            Log.d("surface", "Starting surface Preview..." + mCamera);
            setCamera(camerAR.safeCameraOpen(0));
            if(mCamera!=null) {
                mCamera.setPreviewCallback(frame);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();

            }

        } catch (IOException e) {
            Log.d("surface", "Error setting camera preview: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            Log.d("surface", "destroying surface Preview..." + mCamera);
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                // Call stopPreview() to stop updating the preview surface.
               // mCamera.stopPreview();
                mCamera.release();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
     try {
         Log.d("surface", "change Preview...");
         if (mCamera != null) {
             mCamera.stopPreview();
             mCamera.setDisplayOrientation(90);
             Camera.Parameters parameters = mCamera.getParameters();
             parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
             parameters.setPreviewSize(mSupportedPreviewSizes.get(0).width, mSupportedPreviewSizes.get(0).height);
             requestLayout();
             mCamera.setParameters(parameters);
             mCamera.setPreviewCallback(frame);
             mCamera.setPreviewDisplay(holder);
             // Important: Call startPreview() to start updating the preview surface.
             // Preview must be started before you can take a picture.
             mCamera.startPreview();
         }
     }
     catch (Exception e){
        e.printStackTrace();
     }
    }





    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }






    //this array stores the pixels as hexadecimal pairs
    private int[] pixels;
    private class cam_PreviewCallback implements Camera.PreviewCallback
    {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
          //  Log.d("surface", " Preview callback...");
          //  decodeYUV420SP(pixels, data, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);


        }

    }

    //Method from Ketai project! Not mine! See below...
    void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {       int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)                  r = 0;               else if (r > 262143)
                    r = 262143;
                if (g < 0)                  g = 0;               else if (g > 262143)
                    g = 262143;
                if (b < 0)                  b = 0;               else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }
}
