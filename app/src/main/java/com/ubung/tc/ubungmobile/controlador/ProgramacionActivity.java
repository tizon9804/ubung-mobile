package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaProgramacionAdapter;


public class ProgramacionActivity extends ActionBarActivity {

    public static final String PROGRAMACION = "Programación: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);
        String nameZona = getIntent().getStringExtra(ListaZonasActivity.ZONA);
        setTitle(PROGRAMACION + nameZona);
        initListView();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initListView() {
        ListView g = (ListView) findViewById(R.id.listView_programacion);

        g.setAdapter(new ListaProgramacionAdapter(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intentDescription(position);
            }
        });

    }

    public void intentDescription(int position) {
        finish();
        Intent t = new Intent(this, DescripcionProgramacionActivity.class);
        t.putExtra(MainUbungActivity.POSITION, position + "");
        startActivity(t);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent t = new Intent(this, ListaZonasActivity.class);
        startActivity(t);
    }


}
