package com.ubung.tc.ubungmobile.controlador.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tizon on 14/04/2015.
 */
public class DirectionsAdapter extends Thread {


    private static BufferedReader br;
    private double lon2;
    private double lat2;
    private double lat1;
    private double lon;


    private Handler h;


    public DirectionsAdapter(double lat1, double lon1, double lat2, double lon2, Handler h) {
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lon = lon1;
        this.lon2 = lon2;
        this.h = h;

    }

    public void run() {
        Message m = new Message();
        String poly= getDirections(lat1, lon, lat2, lon2);
        m.obj=decodePoly(poly);
        h.sendMessage(m);

    }

    public Handler getH() {
        return h;
    }

    public static String getDirections(double lat1, double lon1, double lat2, double lon2) {
        Log.e("direcciones", "entrando.." + lat1 + "#" + lon1 + "#" + lat2 + "#" + lon2);
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=true&units=metric";
        Log.e("direcciones", url);

        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);

            InputStream in = response.getEntity().getContent();

            StringBuilder sb = new StringBuilder();

            String line;
            br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
         //   Log.e("ro",sb.toString());
            JSONObject jsonO = new JSONObject(sb.toString());
            JSONArray routes= jsonO.getJSONArray("routes");
            JSONObject array=routes.getJSONObject(0);
            JSONObject overpolylines=array.getJSONObject("overview_polyline");
            String points=overpolylines.getString("points");

            return points;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


}
