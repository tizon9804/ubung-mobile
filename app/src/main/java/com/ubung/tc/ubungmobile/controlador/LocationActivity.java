package com.ubung.tc.ubungmobile.controlador;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ubung.tc.ubungmobile.R;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class LocationActivity extends FragmentActivity{


    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        // carga la capa de botones
        PanelMapFragment panel= new PanelMapFragment();
        FragmentTransaction t= getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_location,panel);
        t.addToBackStack(null);
        t.commit();

        if(map==null){
            map=((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
       if(map!=null){
           //properties con las coordenadas
            map.addMarker(new MarkerOptions().position(new LatLng(4.660708, -74.132137)).title("Casa Tizon"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.660708, -74.132137),1));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 500, null);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setZoomControlsEnabled(true);

        }

    }



}
