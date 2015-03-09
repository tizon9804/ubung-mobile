package com.ubung.tc.ubungmobile.controlador;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.maps.model.Marker;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

public class FragmentDescriptionZona extends Fragment {



    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_description_zona, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            TextView titulozona=(TextView)view.findViewById(R.id.zona_mapa_titulo);
            TextView descripcion=(TextView)view.findViewById(R.id.descripcion_zona_mapa);
            String nombre = bundle.get(LocationActivity.NOMBRE).toString();
            String detalles = bundle.get(LocationActivity.DETALLES).toString();
            titulozona.setText(nombre);
            descripcion.setText(detalles);
        }
        return view;
    }
    



}
