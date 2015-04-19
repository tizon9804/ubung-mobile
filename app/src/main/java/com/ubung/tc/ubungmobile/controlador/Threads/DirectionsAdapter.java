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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
        m.obj = getDirections(lat1, lon, lat2, lon2);
        h.sendMessage(m);
    }

    public Handler getH() {
        return h;
    }

    public static ArrayList getDirections(double lat1, double lon1, double lat2, double lon2) {
        Log.e("direcciones", "entrando.." + lat1 + "#" + lon1 + "#" + lat2 + "#" + lon2);
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=true&units=metric";
        Log.e("direcciones", url);
        String tag[] = {"lat", "lng"};
        ArrayList list_of_geopoints = new ArrayList();
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
            JSONObject jsonO = new JSONObject(sb.toString());
            String r=jsonO.getString("points");
            Log.e("ro",r);

//            json.beginObject();
//            while (json.hasNext()) {
//                //get the element name
//                String name = json.nextName();
//                Log.e("ro",name);
//                if (name.equals("routes")) {
//                    json.beginArray();
//                    while (json.hasNext()) {
//                        Log.e("ro",json.nextString());
//                    }
//
//
//
//                }
            //if the element name is the list of countries then start the array



            //end reader and close the stream


            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            if (doc != null) {
                NodeList nl1, nl2;
                nl1 = doc.getElementsByTagName(tag[0]);
                nl2 = doc.getElementsByTagName(tag[1]);
                if (nl1.getLength() > 0) {
                    list_of_geopoints = new ArrayList();
                    for (int i = 0; i < nl1.getLength(); i++) {
                        Node node1 = nl1.item(i);
                        Node node2 = nl2.item(i);
                        double lat = Double.parseDouble(node1.getTextContent());
                        double lng = Double.parseDouble(node2.getTextContent());
                        list_of_geopoints.add(new LatLng(lat, lng));

                    }
                } else {
                    // No points found
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_of_geopoints;
    }


}
