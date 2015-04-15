package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.local.Deporte;

import java.util.ArrayList;


public class ChooseSportActivity extends FragmentActivity {


    // -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    public static final String POSITION = "position";
    public static final String ID = "id";
    public static final String USER = "usuario";
    public static final String IMAGEBUTTON = "imageButton";

    // -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------

    private Integer[] imgDeportes;

    // -----------------------------------------------------
// Constructor
// -----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sport);
        initView();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initView() {
        //recorre la lista de deportes
        final ArrayList<Deporte> deportes = Singleton.getInstance().darDeportes();
        if (deportes != null) {
            Integer[] identificadores = new Integer[deportes.size()];
            for (int i = 0; i < deportes.size(); i++) {
                //asigna la imagen del deporte a su respectivo boton
                ImageButton button = (ImageButton) findViewById(getResources().getIdentifier(IMAGEBUTTON + i, "id", getPackageName()));
                button.setImageResource(getResources().getIdentifier(deportes.get(i).getNombreArchivoImagen(), "drawable", getPackageName()));
                final int finalI = i;
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            //todo boton seleccionado
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            String usuario = Singleton.getInstance().darPropietario().getNombreUsuario();
                            intentDescription(deportes.get(finalI).getId(), v.getId(), usuario);
                        }

                        return true;
                    }
                });
            }
            imgDeportes = identificadores;
        } else {
            Log.e("Carga deportes", " deportes[]:" + deportes);
            Toast.makeText(this, "Hubo un problema al Cargar Deportes ", Toast.LENGTH_LONG).show();
            imgDeportes = new Integer[]{R.drawable.ic_launcher, R.drawable.ic_launcher};
        }

    }

    public void intentDescription(long id_deporte, long id, String usuario) {
        finish();
        Intent t = new Intent(this, DescriptionSportActivity.class);
        t.putExtra(POSITION, id_deporte + "");
        t.putExtra(ID, id + "");
        t.putExtra(USER, usuario);
        startActivity(t);
    }


}
