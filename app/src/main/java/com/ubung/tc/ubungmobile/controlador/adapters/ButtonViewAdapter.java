package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.util.ArrayList;

public class ButtonViewAdapter extends BaseAdapter {

    private Context cnt;

    public ButtonViewAdapter(Context c) {
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
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
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
        if (deportes != null) {
            Integer[] identificadores = new Integer[deportes.size()];
            for (int i = 0; i < deportes.size(); i++) {
                identificadores[i] = cnt.getResources().getIdentifier(deportes.get(i).getNombreArchivoImagen(), "drawable", cnt.getPackageName());
            }
            return identificadores;
        }
        else{
            Log.e("Carga deportes"," deportes[]:"+ deportes);
            Toast.makeText(cnt, "Hubo un problema al Cargar Deportes ", Toast.LENGTH_LONG).show();
            return new Integer[]{R.drawable.ic_launcher,R.drawable.ic_launcher};
        }
    }

}
