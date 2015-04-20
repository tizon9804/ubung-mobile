package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaZonasAdapter;


public class ListaZonasActivity extends ActionBarActivity {

    public static final String ZONA = "zona";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_zonas);
        initListView();
    }


    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initListView() {
        ListView g = (ListView) findViewById(R.id.listViewZonas);

        g.setAdapter(new ListaZonasAdapter(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  String usuario = Singleton.getInstance().darPropietario().getNombreUsuario();
                TextView nombrezona = (TextView) view.findViewById(R.id.nombreZona);
                TextView idZona = (TextView) view.findViewById(R.id.zona_id);
                intentDescription(nombrezona.getText().toString(),idZona.getText().toString());
            }
        });

    }

    public void intentDescription(String zona, String idZona) {
        finish();
        Intent t = new Intent(this, ProgramacionActivity.class);
        t.putExtra(ZONA, zona);
        t.putExtra(MainUbungActivity.POSITION, idZona);
        startActivity(t);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent t = new Intent(this, LocationActivity.class);
        startActivity(t);
    }
}
