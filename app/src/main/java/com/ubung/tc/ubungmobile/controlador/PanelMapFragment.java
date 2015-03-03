package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubung.tc.ubungmobile.R;

/*
*Clase que configura el panel de botones que se muestra en el mapa
 */
public class PanelMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_panel_map, container, false);
       initButton(view);
        return view;
    }

    private void initButton(View view){

        //boton agregar
        Button button = (Button) view.findViewById(R.id.imageButton_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//boton deporte
        Button button2 = (Button) view.findViewById(R.id.imageButton_sport);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//deporte opciones
        Button button3 = (Button) view.findViewById(R.id.imageButton_options);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//deporte buscar
        Button button4 = (Button) view.findViewById(R.id.imageButton_buscar);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            nextActivity(ListaZonasActivity.class);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent t = new Intent(getActivity(), MainUbungActivity.class);
        startActivity(t);
    }


    public void nextActivity(Class clase){
        Intent t= new Intent(getActivity(),clase);
        startActivity(t);
    }

}
