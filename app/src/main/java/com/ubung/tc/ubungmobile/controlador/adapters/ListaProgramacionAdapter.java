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


import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("ALL")
public class ListaProgramacionAdapter extends BaseAdapter {

    private Context cnt;
    private ArrayList<Evento> eventos;

    public ListaProgramacionAdapter(Context c) {
        cnt = c;
        geteventos();
    }


    @Override
    public int getCount() {
       if(eventos!=null)
           return eventos.size();
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
            rowView = inflater.inflate(R.layout.list_row_programacion, parent, false);
        }

        // Set data into the view.
        TextView nombreZona = (TextView) rowView.findViewById(R.id.nombre_deporte_programacion);
        TextView inscritpsZona = (TextView) rowView.findViewById(R.id.inscritos_programacion);
        TextView horaZona = (TextView) rowView.findViewById(R.id.hora_programacion);
        Evento z=  eventos.get(position);
        Date d= z.getFechaHora();
        String hora= d.getHours()+":"+d.getMinutes();
        nombreZona.setText(z.getDeporte().getNombre());
        horaZona.setText(hora);
        inscritpsZona.setText(": " + z.getInscritos().size());
        return rowView;
    }

    // obtener las imagenes de los deportes
    private void geteventos() {
        eventos= Singleton.getInstance().darEventos();
        if (eventos == null) {
            Log.e("Carga eventos"," eventos[]:"+ null);
            Toast.makeText(cnt, "Hubo un problema al Cargar eventos ", Toast.LENGTH_LONG).show();
        }

    }

}
