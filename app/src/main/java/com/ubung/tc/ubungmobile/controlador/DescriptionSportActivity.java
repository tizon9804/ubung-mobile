package com.ubung.tc.ubungmobile.controlador;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;


public class DescriptionSportActivity extends ActionBarActivity {

    // -----------------------------------------------------
// Constantes
// -----------------------------------------------------

    public final static String LAST = "last";
    // -----------------------------------------------------
// Atributos
// -----------------------------------------------------

    private String usuario;
    private Deporte deporteCrear;
    private long celular;

    // -----------------------------------------------------
// CONSTRUCTOR
// -----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_sport);
        InitLabels();
        initButtons();
    }
    // -----------------------------------------------------
// INICIALIZADORES
// -----------------------------------------------------

    private void InitLabels() {

        Intent t = getIntent();
        long position = Long.parseLong(t.getStringExtra(MainUbungActivity.POSITION));
        String user = t.getStringExtra(MainUbungActivity.USER);
        String phone= t.getStringExtra(MainUbungActivity.PHONE);
        String deporte = "Basketball";
        String descripcion = "hola";
        Deporte deporteU = Singleton.getInstance().darDeporte(position);


        if (deporteU != null) {
            deporte = deporteU.getNombre();
            descripcion = deporteU.getDescripcion();
            TextView txtv = (TextView) findViewById(R.id.title_description_sport);
            txtv.setText("Enhorabuena! " + user + " has escogido " + deporte + ".");
            ImageView image = (ImageView) findViewById(R.id.image_sport_description);
            Integer imagePath = getResources().getIdentifier(deporteU.getNombreArchivoImagen(), "drawable", getPackageName());
            image.setImageResource(imagePath);
            TextView txtdesc = (TextView) findViewById(R.id.description_sport);
            txtdesc.setText("Descripción:\n" + descripcion);
        } else {
            Toast.makeText(this, "Hubo un problema al cargar el deporte.", Toast.LENGTH_LONG).show();
        }

        celular=Long.parseLong(phone);
        usuario = user;
        deporteCrear = deporteU;
    }

    private void initButtons() {
        final Button start = (Button) findViewById(R.id.btn_continue);

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
                    start.setTextColor(getResources().getColor(R.color.holo_green_light));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    start.setTextColor(getResources().getColor(R.color.white));

                    try {
                        Singleton.getInstance().modificarPropietario(usuario,celular, deporteCrear);




                    } catch (ExcepcionPersistencia excepcionPersistencia) {
                        Toast.makeText(getBaseContext(), "Hubo un problema al Crear el usuario " + usuario, Toast.LENGTH_LONG).show();
                    }
                }
                return false;
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
        Intent t = new Intent(this, MainUbungActivity.class);
        t.putExtra("last", true);

    }


}
