package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.Threads.AnimationAugmentedReality;
import com.ubung.tc.ubungmobile.controlador.Threads.CameraAR;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

public class AugmentedReality_Activity extends Activity implements SensorEventListener {

    private CameraAR cam;
    private FrameLayout prevt;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float compass;
    private float ejez;
    private float ejex;
    private GoogleMap map;
    private AnimationAugmentedReality aR;
    private Zona zonaCercana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_reality_);
        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.map=PanelMapFragment.map;
        initCamera();
        initZonaRendered();
    }

    private void initZonaRendered() {

        final ImageView zona=(ImageView) findViewById(R.id.zonaViewRendered);
        final TextView txtzona=(TextView) findViewById(R.id.distancia_zona_ar);
        Handler handposition=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                RelativeLayout.LayoutParams r=(RelativeLayout.LayoutParams)msg.obj;
                zona.setLayoutParams(r);
                r.topMargin=r.topMargin+250;
                txtzona.setLayoutParams(r);
                txtzona.setText(r.leftMargin+" metros");
                return true;
            }
        });

        aR= new AnimationAugmentedReality(handposition,zona,prevt);
        aR.start();



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

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
        Camera mCamera=cam.getmCamera();
        try {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            cam.getmPreview().getHolder().removeCallback(cam.getmPreview());
            mCamera.release();
            //mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    float[] mGravity;
    float[] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                /*
                y
                |
                |     z
                |    -
                |   -
                |  -
                | -
                |-
                -----------------------x

                 */

                compass = orientation[0]; // giro en   eje y 0 norte 3.15 sur 1.575 oriente -1.575 occidente
                ejez = orientation[1]; // giro en  eje z -1.5 a 1.5
                ejex = orientation[2]; // giro en  eje x giro a la derecha 0 a 3 izquierda de 0 a -3




               try {
                   double angulo =orientacionZonaCercana();

                   if(angulo>-4) {
                       // angulo=3;
                       double diferencia = Math.abs(compass - angulo);
                       double tolerancia = 1;
                       double toleranciaT = 0.3;
                    if(diferencia<toleranciaT){
                        aR.setPosition(350, 800);
                    }
                    else if (diferencia<tolerancia){

                        if(compass<angulo){

                            if(ejez>1||ejez<-1) {
                                int pos=(int)(350-(diferencia*200));
                                aR.setPosition(pos, 800);
                            }
                            else {
                                int pos=(int)(800-(diferencia*200));
                                aR.setPosition(350, pos);
                            }
                        }

                        else if(compass>angulo){

                            if(ejez>1||ejez<-1) {
                                int pos=(int)(350+(diferencia*200));
                                aR.setPosition(pos, 800);
                            }
                            else {
                                int pos=(int)(800+(diferencia*200));
                                aR.setPosition(350, pos);
                            }
                        }
                        else{
                                aR.setPosition(350, 800);
                        }

                    }
                       else{
                        if(ejez>1||ejez<-1) {

                            aR.setPosition(3000, 800);
                        }
                        else {

                            aR.setPosition(350, 5000);
                        }
                    }





                   }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private double orientacionZonaCercana() throws ParseException {
       LatLng ubicacion= new LatLng(0,0);
        // se obtiene la ubicación ya sea directamente del gps o de la ultima posicion registrada
        if(map.getMyLocation()!=null) {
          double  lat=map.getMyLocation().getLatitude();
          double  lon=map.getMyLocation().getLongitude();
           ubicacion= new LatLng(lat,lon);
            double[] loc=new double[3];
            loc[0]=lat;
            loc[1]=lon;
            loc[2]=17;
            Singleton.getInstance().darPropietario().setUltimaUbicacion(loc);
        }
        else{
            Usuario propietario=Singleton.getInstance().darPropietario();
            if(propietario!=null){
                ubicacion= new LatLng( propietario.getUltimaUbicacion()[0],propietario.getUltimaUbicacion()[1]);
            }
        }
        // recorrido para hallar la zona mas cercana
        double minimo=10000;
        if(zonaCercana == null) {
            ArrayList<Zona> zonas = Singleton.getInstance().darZonas();

            for (Zona z : zonas) {
                double[]loczo=z.getLatLongZoom();
                double lat=loczo[0];
                double lon=loczo[1];
                LatLng geoZona=new LatLng(lat,lon);
                double actual=cercaZona(ubicacion,geoZona);
                if (actual<minimo) {
                    zonaCercana=z;
                    minimo=actual;
                }
            }
        }
        // calculo de orientacion magnetica de mi ubicacion a la zona
        if(zonaCercana!=null){
            double[]loczo=zonaCercana.getLatLongZoom();
            double lat=loczo[0];
            double lon=loczo[1];
            LatLng geoZona=new LatLng(lat,lon);
           double angulo= anguloGeoZona(ubicacion,geoZona);
           return  angulo;
        }

        return -4;

    }

    private double anguloGeoZona(LatLng ubicacion, LatLng geoZona) {
        double x = geoZona.latitude-ubicacion.latitude ;
        double y = geoZona.longitude-ubicacion.longitude ;
       return Math.atan2(x,y);
    }

    public double cercaZona(LatLng pos, LatLng zona) {

        double d = 0;
        double x = Math.pow((zona.latitude - pos.latitude), 2);
        double y = Math.pow(zona.longitude - pos.longitude, 2);
        d = Math.sqrt(x + y) * 100;
        d = d * 1000;
        // Log.e("calculo entro en zona", "La distancia es de:" + d);

        return d;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }




}
