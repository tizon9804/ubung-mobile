package com.ubung.tc.ubungmobile.controlador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ButtonViewAdapter;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaZonasAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;

public class ListaZonasActivity extends ActionBarActivity {

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
                String usuario = Singleton.getInstance().darPropietario().getNombreUsuario();
                intentDescription();
            }
        });

    }

    public void intentDescription() {
        finish();
        Intent t = new Intent(this, ProgramacionActivity.class);
        startActivity(t);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent t = new Intent(this, LocationActivity.class);
        startActivity(t);
    }
}
