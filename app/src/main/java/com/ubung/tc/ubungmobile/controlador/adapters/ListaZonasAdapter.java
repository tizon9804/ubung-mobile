package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;


import java.util.ArrayList;

public class ListaZonasAdapter extends BaseAdapter {

    private Context cnt;
    private ArrayList<Zona> zonas;

    public ListaZonasAdapter(Context c) {
        cnt = c;
        getZonas();
    }


    @Override
    public int getCount() {
        return zonas.size();
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

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) cnt
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_row_zonas, parent, false);
        }

        // Set data into the view.
        TextView nombreZona = (TextView) rowView.findViewById(R.id.nombreZona);
        Zona z = zonas.get(position);
        nombreZona.setText(z.getNombre());
        return rowView;
    }

    // obtener las imagenes de los deportes
    private void getZonas() {
        try {
            zonas = Singleton.getInstance().darZonas();
        } catch (ParseException e) {
           Log.e("ListaZonasAdapter",e.getMessage());
        }
        if (zonas == null) {
            Log.e("Carga zonas", " zonas[]:" + zonas);
            Toast.makeText(cnt, "Hubo un problema al Cargar zonas ", Toast.LENGTH_LONG).show();
        }

    }

}
