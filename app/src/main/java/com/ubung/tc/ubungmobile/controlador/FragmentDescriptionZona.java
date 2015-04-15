package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.ubung.tc.ubungmobile.R;

public class FragmentDescriptionZona extends Fragment {



    private View view;
    private String nombre;
    private String detalles;
    private String pos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_description_zona, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            TextView titulozona=(TextView)view.findViewById(R.id.zona_mapa_titulo);
            TextView descripcion=(TextView)view.findViewById(R.id.descripcion_zona_mapa);
             nombre = bundle.get(LocationActivity.NOMBRE).toString();
             detalles = bundle.get(LocationActivity.DETALLES).toString();
            pos=bundle.get(LocationActivity.POS).toString();
            titulozona.setText(nombre);
            descripcion.setText(detalles);
        }
        initBUtton();
        return view;
    }

    private void initBUtton() {
        //boton agregar
        Button button = (Button) view.findViewById(R.id.button_add_zonas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity();
                Intent t = new Intent(getActivity(),EventoActivity.class);
                startActivity(t);

            }
        });

        //boton eventos
        final Button button2 = (Button) view.findViewById(R.id.button_eventos_zona);
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button2.setTextColor(getResources().getColor(R.color.holo_green_light));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    button2.setTextColor(getResources().getColor(R.color.white));
                    nextActivity();
                    Intent t = new Intent(getActivity(), ProgramacionActivity.class);
                    t.putExtra(ListaZonasActivity.ZONA, nombre);
                    t.putExtra(MainUbungActivity.POSITION,pos);
                    startActivity(t);
                }
                return false;
            }
        });
    }
    public void nextActivity() {
        try {
            finalize();
            getActivity().finish();            
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
