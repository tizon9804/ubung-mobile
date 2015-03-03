package com.ubung.tc.ubungmobile.controlador;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.ubung.tc.ubungmobile.R;


public class LocationActivity extends FragmentActivity {


    private GoogleMap map;

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
         crearZonas();
        }

    }

    private void crearZonas() {
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(4.660594, -74.133360))
                .radius(30)
                .fillColor(R.color.holo_orange_light)
                .strokeColor(R.color.holo_orange_dark); // In meters

// Get back the mutable Circle
        Circle circle =map.addCircle(circleOptions);

        circleOptions = new CircleOptions()
                .center(new LatLng(4.660186, -74.130386))
                .radius(30)
                .fillColor(R.color.holo_orange_light)
                .strokeColor(R.color.holo_orange_dark); // In meters

// Get back the mutable Circle
       circle =map.addCircle(circleOptions);

    }

    public void darUbicacion(View v){


        if(map.getMyLocation()==null || ! map.isMyLocationEnabled()){
            Toast.makeText(this, "No se encontró su ubicación, por favor verifique su GPS.", Toast.LENGTH_LONG).show();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.660708, -74.132137), 19));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
        else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), 19));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
    }


}
