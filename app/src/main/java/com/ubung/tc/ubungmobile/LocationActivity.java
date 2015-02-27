package com.ubung.tc.ubungmobile;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ubung.tc.ubungmobile.util.SystemUiHider;


import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LocationActivity extends FragmentActivity{


    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        if(map==null){
            map=((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
       if(map!=null){
           //properties con las coordenadas
            map.addMarker(new MarkerOptions().position(new LatLng(4.660708, -74.132137)).title("Casa Tizon"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.660708, -74.132137),1));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 10000, null);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setZoomControlsEnabled(true);
        }

    }



}
