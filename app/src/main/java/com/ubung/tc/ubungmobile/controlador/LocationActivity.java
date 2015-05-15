package com.ubung.tc.ubungmobile.controlador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.Threads.AnimationZona;
import com.ubung.tc.ubungmobile.controlador.Threads.DirectionsAdapter;
import com.ubung.tc.ubungmobile.controlador.Threads.NotifyZonaCercana;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LocationActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {


    private static final double CENTRAR = 0.0010;
    public static final String NOMBRE = "NOMBRE_ZONA";
    public static final String DETALLES = "detalles_zona";
    public static final String POS = "pos";
    private GoogleMap map;
    private LatLng ultimaPosicion;
    private ArrayList<Marker> markers;
    protected boolean active = true;
    protected int ubungTime = 10000;
    private Thread mapzoneThread;
    private ArrayList<Zona> zonas;
    private static boolean start;
    private FragmentDescriptionZona zona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nombreZona=getIntent().getStringExtra(NOMBRE);
        // carga la capa de botones
        final PanelMapFragment[] panel = {new PanelMapFragment()};
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_location, panel[0]);
        t.addToBackStack(null);
        t.commit();
        setContentView(R.layout.activity_location);
        start=true;
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (map != null) {

            panel[0].setMap(map);
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 500, null);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
          //  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.setMyLocationEnabled(true);

            map.getUiSettings().setMyLocationButtonEnabled(false);
            //map.setTrafficEnabled(true);
            crearZonas();
            //properties con las coordenadas
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    panel[0] = new PanelMapFragment();
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.activity_location, panel[0]);
                    t.addToBackStack(null);
                    t.commit();

                }
            });
            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    animation(latLng, latLng.latitude, latLng.longitude);
                }
            });
        }

        getParseLocalization();
        notifyZonasCercanas();
        if(nombreZona!=null){
            for (Marker m: markers){
                if(m.getTitle().equals(nombreZona)){
                    onMarkerClick(m);
                    break;
                }
            }
            start=false;
        }

    }

    private void getParseLocalization(){
        if (Singleton.getInstance().darPropietario()!=null && Singleton.getInstance().darPropietario().getUltimaUbicacion()!=null) {
            double[] localizacion=Singleton.getInstance().darPropietario().getUltimaUbicacion();
            double lat = localizacion[0];
            double lonl = localizacion[1];
            LatLng latlng = new LatLng(lat, lonl);
            ultimaPosicion=latlng;
            CameraPosition p = new CameraPosition(latlng, 17, 0, 0);
            map.animateCamera(CameraUpdateFactory.newCameraPosition(p), 500, null);
        }
    }

    private void notifyZonasCercanas() {
        try {
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                final String[] anteriorZona = {""};
                final String[] id = {""};
                ArrayList<Zona> zs = Singleton.getInstance().darZonas();

                @Override
                public void onMyLocationChange(Location location) {

                    double l;
                    double lonl;
                    CameraPosition p;
                    LatLng latlng;

                    if (map.getMyLocation() != null && start) {
                        start=false;
                        l = map.getMyLocation().getLatitude();
                        lonl = map.getMyLocation().getLongitude();
                        latlng = new LatLng(l - CENTRAR, lonl);
                        ultimaPosicion=latlng;
                        p = new CameraPosition(latlng, 17, 0, 0);
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(p), 500, null);
                        double[] loc=new double[3];
                        loc[0]=l;
                        loc[1]=lonl;
                        loc[2]=17;
                        if(Singleton.getInstance().darPropietario()!=null) {
                            Singleton.getInstance().darPropietario().setUltimaUbicacion(loc);
                        }
                    }
                    // Log.e("Change","Cambio mi posicion");

                    Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message m) {
                            Zona z = (Zona) m.obj;
                            if (!z.getNombre().equals(anteriorZona[0])) {
                                Log.e("Nuevo push", "#### push");
                                ParsePush p = new ParsePush();
                                p.setChannel(z.getId() + "");
                                p.setQuery(ParseInstallation.getQuery());
                                p.setMessage("Estas Cerca de La zona " + z.getNombre());
                                p.sendInBackground();
                                anteriorZona[0] = z.getNombre();
                                id[0] = z.getId() + "";
                            }
                        }
                    };
                    Location loc=map.getMyLocation();
                    if(loc!=null) {
                    NotifyZonaCercana n = new NotifyZonaCercana(loc, h, zs);
                    n.start();
                    }
                }
            });
        } catch (ParseException e) {
            Log.e("NotifyZonas",e.getMessage());
        }
    }

    /*
    *crea las zonas a partir de markers de google, y guarda sus marker en un arreglo
     */
    private void crearZonas() {
        map.setOnMarkerClickListener(this);


        try {
            zonas = Singleton.getInstance().darZonas();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        markers = new ArrayList<Marker>();
        for (Zona z : zonas) {
            LatLng latlng = new LatLng(z.getLatLongZoom()[0], z.getLatLongZoom()[1] - 0.0002);

            Marker m = map.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(z.getNombre())
                    .snippet(z.getNombre())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.zona_imagen)));

            markers.add(m);

        }


    }

    private void crearZonasTemp(){
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
            getParseLocalization();
        } else {
            double lat=map.getMyLocation().getLatitude();
            double lon=map.getMyLocation().getLongitude();
            ultimaPosicion = new LatLng(lat,lon);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ultimaPosicion, 17));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
            double[] loc=new double[3];
            loc[0]=lat;
            loc[1]=lon;
            loc[2]=17;
            if(Singleton.getInstance().darPropietario()!=null) {
                Singleton.getInstance().darPropietario().setUltimaUbicacion(loc);
            }

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

        final double l = marker.getPosition().latitude;
        final double lonl = marker.getPosition().longitude;

        LatLng latlng = new LatLng(l - CENTRAR, lonl);


        //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
        CameraPosition p = new CameraPosition(latlng, 17, 0, 0);

        map.animateCamera(CameraUpdateFactory.newCameraPosition(p), 500, null);
        String detalles = "En esta zona se practica: ";
        int i = 0;

        HashMap deportes = new HashMap();
        Zona zonaActual=new Zona();
        for (Zona z : zonas) {
            if (z.getNombre().equals(marker.getTitle())) {
                try {ArrayList<Evento> eventos = Singleton.getInstance().buscarEventos(z.getId());
                zonaActual=z;


                    if(eventos.size()==0)detalles="\n\nNo hay registro de deportes para esta zona.";
                for (Evento e : eventos) {
                    try {
                        if (!deportes.containsKey(e.getDeporte().getNombre())) {
                            detalles += "\n " + e.getDeporte().getNombre();
                            deportes.put(e.getDeporte().getNombre(), true);
                        }
                    } catch (ParseException e1) {
                        Log.e("LocationActivity",e1.getMessage());
                    }

                }
                break;
                } catch (ParseException e) {
                    Log.e("LocationActivity",e.getMessage());
                }
            }
            i++;
        }

        zona = new FragmentDescriptionZona();
        Bundle bundle = new Bundle();
        bundle.putString(NOMBRE,zonaActual.getNombre());
        bundle.putString(DETALLES, detalles);
        bundle.putString(POS,zonaActual.getId());
        zona.setArguments(bundle);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_location, zona);
        t.addToBackStack(null);
        t.commit();
        animation(marker.getPosition(),l,lonl);




        return true;
    }

    public void animation(LatLng marker, final double l, final double lonl){
        final LatLng mar = marker;
        final Handler hand = new Handler() {
            @Override
            public void handleMessage(Message m) {
                int radious = (int) m.obj;
                if (radious == -1) {
                    map.clear();
                    crearZonasTemp();
                    if (Singleton.getInstance().hayConexion()) {
                        getDirections(ultimaPosicion.latitude, ultimaPosicion.longitude, l, lonl);
                    }

                }
                else {
                        map.clear();
                        CircleOptions circle = new CircleOptions();
                        circle.center(mar);
                        circle.radius(radious);
                        int opacidad = (100 - radious);
                        circle.strokeColor(Color.argb(opacidad, 255, 147, 23));
                        circle.strokeWidth(opacidad);
                        map.addCircle(circle);

                }

            }
        };

        AnimationZona a = new AnimationZona(hand);
        a.start();


    }

    public void getDirections(double lat1, double lon1, double lat2, double lon2) {

        final LatLng origen = new LatLng(lat1, lon1);
        final LatLng destino = new LatLng(lat2, lon2);

        if (!cercaZona(origen, destino)) {
            Handler h;
            h = new Handler() {
                @Override
                public void handleMessage(Message m) {
                    drawPrimaryLinePath( (List<LatLng>) m.obj, origen, destino);
                   // Log.e("direcciones", "tamaño de la lista de nodos: " + ((ArrayList<LatLng>) m.obj).size());
                }
            };
            DirectionsAdapter d = new DirectionsAdapter(lat1, lon1, lat2, lon2, h);
            d.start();
        } else {
            Toast.makeText(this, "Esta muy cerca de su destino!", Toast.LENGTH_LONG).show();
        }
    }

    public final synchronized void drawPrimaryLinePath(List<LatLng> listLocsToDraw, LatLng origen, LatLng destino) {
        map.clear();
        crearZonas();

        PolylineOptions polyop = new PolylineOptions();

        boolean term = false;
        for (int i = 0; i < listLocsToDraw.size() && !term; i++) {
            LatLng l = listLocsToDraw.get(i);
            if (cercaZona(l, destino)) {
                term = true;
            }
            polyop.add(l);
        }

        polyop.geodesic(true);
        polyop.color(Color.rgb(100, 147, 23));
        polyop.width(20);
        map.addPolyline(polyop);

    }


    public boolean cercaZona(LatLng pos, LatLng zona) {

        double d = 0;
        double x = Math.pow((zona.latitude - pos.latitude), 2);
        double y = Math.pow(zona.longitude - pos.longitude, 2);
        d = Math.sqrt(x + y) * 100;
        d = d * 1000;
       // Log.e("calculo dist", "el punto es de :" + zona.latitude+"##"+zona.longitude);
        if (d < 50) {
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
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

    public void onResume(){
        super.onResume();
        start=true;
    }

}
