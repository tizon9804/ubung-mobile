package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.local.Usuario;

/*
*Clase que configura el panel de botones que se muestra en el mapa
 */
public class PanelMapFragment extends Fragment {

    public final static String NORMAL = "_normal";
    public final static String FOCUSED = "_focused";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_panel_map, container, false);
        initButton(view);
        return view;
    }

    private void initButton(View view) {

        //boton agregar
        Button button = (Button) view.findViewById(R.id.imageButton_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity(EventoActivity.class);

            }
        });
//boton deporte
        final Button button2 = (Button) view.findViewById(R.id.imageButton_sport);
        final Usuario u = Singleton.getInstance().darPropietario();
        if (u != null) {
            button2.setBackgroundResource(getResources().getIdentifier(u.getDeporte().getNombreArchivoImagen() + NORMAL, "drawable", getActivity().getPackageName()));
            button2.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        button2.setBackgroundResource(getResources().getIdentifier(u.getDeporte().getNombreArchivoImagen() + FOCUSED, "drawable", getActivity().getPackageName()));

                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        button2.setBackgroundResource(getResources().getIdentifier(u.getDeporte().getNombreArchivoImagen() + NORMAL, "drawable", getActivity().getPackageName()));
                        nextActivity(ChooseSportActivity.class);
                    }
                    return false;
                }
            });
        }
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


    public void nextActivity(Class clase) {
        try {
            finalize();
            getActivity().finish();
            Intent t = new Intent(getActivity(), clase);
            startActivity(t);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
