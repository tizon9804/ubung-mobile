package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.util.ArrayList;

public class ButtonAdapterView extends BaseAdapter {

    private Context cnt;

    public ButtonAdapterView(Context c) {
        cnt = c;
    }


    @Override
    public int getCount() {
        return getDeportes().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(cnt);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(getDeportes()[position]);
        return imageView;
    }

    // obtener las imagenes de los deportes
    private Integer[] getDeportes() {
        ArrayList<Deporte> deportes = Singleton.getInstance().darDeportes();

        Integer[] identificadores = new Integer[deportes.size()];
        for (int i = 0; i < deportes.size(); i++) {
            identificadores[i] = cnt.getResources().getIdentifier(deportes.get(i).getNombreArchivoImagen(), "drawable", cnt.getPackageName());
        }

        return identificadores;
    }


}
