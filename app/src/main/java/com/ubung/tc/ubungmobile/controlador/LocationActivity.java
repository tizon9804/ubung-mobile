package com.ubung.tc.ubungmobile.controlador;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;


public class LocationActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener{


    private GoogleMap map;
    private ArrayList<Marker> markers;

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

    private void crearZonas() {
        map.setOnMarkerClickListener(this);
       ArrayList<Zona> zonas= Singleton.getInstance().darZonas();
        markers=new ArrayList<Marker>();
        for(Zona z:zonas){
        LatLng latlng= new LatLng(z.getLatLongZoom()[0],z.getLatLongZoom()[1]);
          Marker m=map.addMarker(new MarkerOptions()
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


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.zona_imagen_focused));
        marker.showInfoWindow();
        return true;
    }
}
