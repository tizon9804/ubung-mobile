package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;


public class LocationActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {


    private static final double CENTRAR = 0.0010 ;
    public static final String NOMBRE ="NOMBRE_ZONA" ;
    public static final String DETALLES = "detalles_zona";
    public static final String POS = "pos";
    private GoogleMap map;
    private ArrayList<Marker> markers;
    protected boolean active = true;
    protected int ubungTime = 10000;
    private Thread mapzoneThread;
    private ArrayList<Zona> zonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // carga la capa de botones
        PanelMapFragment panel = new PanelMapFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_location, panel);
        t.addToBackStack(null);
        t.commit();
        setContentView(R.layout.activity_location);


        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (map != null) {
            //properties con las coordenadas
            // map.addMarker(new MarkerOptions().position(new LatLng(4.660708, -74.132137)).title("Casa Tizon"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.660708, -74.132137), 1));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 500, null);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            crearZonas();
        }

    }

    /*
    *crea las zonas a partir de markers de google, y guarda sus marker en un arreglo
     */
    private void crearZonas() {
        map.setOnMarkerClickListener(this);
        zonas = Singleton.getInstance().darZonas();
        markers = new ArrayList<Marker>();
        for (Zona z : zonas) {
            LatLng latlng = new LatLng(z.getLatLongZoom()[0], z.getLatLongZoom()[1]);
            Marker m = map.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(z.getNombre())
                    .snippet(z.getNombre())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.zona_imagen)));

            markers.add(m);
        }


    }


    public void darUbicacion(View v) {


        if (map.getMyLocation() == null || !map.isMyLocationEnabled()) {
            Toast.makeText(this, "No se encontró su ubicación, por favor verifique su GPS.", Toast.LENGTH_LONG).show();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.660708, -74.132137), 19));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), 19));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
    }

    /*
    * evento que maneja cuando se hace click sobre una zona
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        for (Marker m : markers) {
            m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.zona_imagen));
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.zona_imagen_focused));
        marker.showInfoWindow();

        double l= marker.getPosition().latitude;
        double lonl= marker.getPosition().longitude;

        LatLng latlng= new LatLng(l-CENTRAR,lonl);



        //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
        CameraPosition p = new CameraPosition(latlng, 17, 0, 0);

        map.animateCamera(CameraUpdateFactory.newCameraPosition(p), 500, null);
        String detalles="En esta zona se practica: ";
        String nombre="zona";
        int pos=0;
        int i=0;
        for(Zona z:zonas){
            if(z.getNombre().equals(marker.getTitle())){
                ArrayList<Evento> eventos = Singleton.getInstance().darEventos(z.getId());
                nombre=z.getNombre();
                pos=i;
                for (Evento e:eventos){
                    detalles+="\n "+e.getDeporte().getNombre();

                }
                break;
            }
            i++;
        }

        FragmentDescriptionZona zona = new FragmentDescriptionZona();
        Bundle bundle = new Bundle();
        bundle.putString(NOMBRE,nombre);
        bundle.putString(DETALLES,detalles);
        bundle.putString(POS, pos + "");
        zona.setArguments(bundle);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_location, zona);
        t.addToBackStack(null);
        t.commit();


        
       // animation(marker);


        return true;
    }

    private void animation(final Marker m) {
        final android.os.Handler handler = new android.os.Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;

        Projection proj = map.getProjection();
        final LatLng markerLatLng = m.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                m.setPosition(new LatLng(lat, lng));
                // m.setIcon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("animacion" + elapsed%11, "drawable", getPackageName())));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }


            }
        });


    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            getFragmentManager().popBackStack();
        }
    }

}
