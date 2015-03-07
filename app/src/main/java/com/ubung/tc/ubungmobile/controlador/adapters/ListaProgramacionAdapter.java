package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

public class ListaProgramacionAdapter extends BaseAdapter {

    private Context cnt;

    public ListaProgramacionAdapter(Context c) {
        cnt = c;
    }


    @Override
    public int getCount() {
       if(geteventos()!=null)
           return geteventos().size();
           else
           return 0;


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
            rowView = inflater.inflate(R.layout.activity_programacion, parent, false);
        }

        // Set data into the view.
        TextView nombreZona = (TextView) rowView.findViewById(R.id.nombreZona);
        Evento z=  geteventos().get(position);
        nombreZona.setText(z.getDeporte().getNombre());
        return rowView;
    }

    // obtener las imagenes de los deportes
    private ArrayList<Evento> geteventos() {
        ArrayList<Evento> eventos= Singleton.getInstance().darEventos();
        if (eventos == null) {
            Log.e("Carga eventos"," eventos[]:"+ eventos);
            Toast.makeText(cnt, "Hubo un problema al Cargar eventos ", Toast.LENGTH_LONG).show();
        }
        return eventos;
    }

}
