package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaProgramacionAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.local.Zona;


public class ProgramacionActivity extends ActionBarActivity {

    public static final String PROGRAMACION = "Programación: ";
    private String nameZona;
    private Zona z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);
        nameZona = getIntent().getStringExtra(ListaZonasActivity.ZONA);
        int pos=Integer.parseInt(getIntent().getStringExtra(MainUbungActivity.POSITION));
        z=(Zona)Singleton.getInstance().darZonas().get(pos);
        setTitle(PROGRAMACION + nameZona);
        initListView();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initListView() {
        ListView g = (ListView) findViewById(R.id.listView_programacion);

        g.setAdapter(new ListaProgramacionAdapter(this,z.getId()));
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
        t.putExtra(ListaZonasActivity.ZONA, nameZona);
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
