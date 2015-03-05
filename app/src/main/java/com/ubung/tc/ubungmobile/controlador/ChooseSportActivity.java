package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ButtonAdapterView;
import com.ubung.tc.ubungmobile.modelo.Singleton;


public class ChooseSportActivity extends FragmentActivity {


    // -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    public static final String POSITION = "position";
    public static final String ID = "id";
    public static final String USER = "usuario";

    // -----------------------------------------------------
// Constructor
// -----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sport);
        initGridView();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initGridView() {
        GridView g = (GridView) findViewById(R.id.grid_button_view);
        final String usuario = Singleton.getInstance().darPropietario().getNombreUsuario();
        g.setAdapter(new ButtonAdapterView(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Hubo un problema al cargar el deporte.", Toast.LENGTH_LONG).show();
           intentDescription(position, id, usuario);
            }
        });

    }

    public void intentDescription(int position, long id, String usuario) {
        finish();
        Intent t = new Intent(this, DescriptionSportActivity.class);
        t.putExtra(POSITION, position+"");
        t.putExtra(ID, id+"");
        t.putExtra(USER, usuario);
        startActivity(t);
    }


}
