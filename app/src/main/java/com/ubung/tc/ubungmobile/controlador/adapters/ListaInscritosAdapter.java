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
import com.ubung.tc.ubungmobile.modelo.persistencia.Tupla;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

import java.util.ArrayList;
import java.util.Date;


public class ListaInscritosAdapter extends BaseAdapter {

    private Context cnt;
    private Evento evento;
    private ArrayList<Tupla<Usuario, Date>> usuarios;
   // private ArrayList usuarios;

    public ListaInscritosAdapter(Context c, Evento v) {
        cnt = c;
        evento=v;
        getUsuarios();

    }


    @Override
    public int getCount() {
        if (usuarios != null)
            return usuarios.size();
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
            rowView = inflater.inflate(R.layout.list_row_inscritos, parent, false);
        }

        // Set data into the view.
        TextView inscritosZona = (TextView) rowView.findViewById(R.id.inscritos_nombre);
        Usuario z = usuarios.get(position).getIzq();
        Log.e("inscritos:",z+"");
        inscritosZona.setText(z.getNombreUsuario());
        return rowView;
    }

    // obtener las imagenes de los usuarios
    private void getUsuarios() {
        usuarios = evento.getInscritos();
        Log.e("inscritos:",usuarios.size()+"");
        if (usuarios == null) {
            Log.e("Carga inscritos", " inscritos[]:" + null);
            Toast.makeText(cnt, "Hubo un problema al Cargar Inscritos ", Toast.LENGTH_LONG).show();
        }

    }

}
