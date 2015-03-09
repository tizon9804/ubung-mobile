package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaInscritosAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;


import java.util.ArrayList;
import java.util.Date;


public class DescripcionProgramacionActivity extends ActionBarActivity {

    private static final String INSCRITOS_TITULO = "Inscritos: ";
    private ArrayList<Evento> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_programacion);
        int position=Integer.parseInt(getIntent().getStringExtra(MainUbungActivity.POSITION));
        eventos=Singleton.getInstance().darEventos();
        if(eventos!=null) {
            Evento v = eventos.get(position);
            //llamado a todos los elementos graficos
            TextView organiza = (TextView) findViewById(R.id.txt_organiza);
            ImageView img = (ImageView) findViewById(R.id.image_descripcion_programacion);
            TextClock hora = (TextClock) findViewById(R.id.textClock_descripcion_programacion);
            ListView inscritos=(ListView) findViewById(R.id.listView_inscritos);
            TextView inscritosTitulo = (TextView) findViewById(R.id.title_inscritos_descripcion_programacion);
            //carga de informacion a los elementos graficos
            ListaInscritosAdapter l= new ListaInscritosAdapter(this,v);
            inscritos.setAdapter(l);
            inscritos.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            organiza.setText(v.getOrganizador().getNombreUsuario());
            img.setImageResource(getResources().getIdentifier(v.getDeporte().getNombreArchivoImagen(), "drawable", getPackageName()));
            Date d = v.getFechaHora();
            String horap = d.getHours() + ":" + d.getMinutes();
            hora.setText(horap);
            inscritosTitulo.setText(INSCRITOS_TITULO + v.getInscritos().size());
        }
        initButtons();
    }

    private void initButtons() {
        final Button start = (Button) findViewById(R.id.button_inscripcion);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.setTextColor(getResources().getColor(R.color.holo_orange_light));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    start.setTextColor(getResources().getColor(R.color.white));
                    nextActivity();
                }
                return true;
            }
        });

    }


    // -----------------------------------------------------
// CONEXION SIGUIENTE ACTIVIDAD
// -----------------------------------------------------

    private void nextActivity() {
        finish();
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent t = new Intent(this, ProgramacionActivity.class);
        t.putExtra(ListaZonasActivity.ZONA, getIntent().getStringExtra(ListaZonasActivity.ZONA));
        startActivity(t);

    }

}
