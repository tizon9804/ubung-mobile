package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaProgramacionAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;


public class ProgramacionActivity extends ActionBarActivity {

    public static final String PROGRAMACION = "Programación: ";
    private String nameZona;
    private Zona z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);
        //nombre de la zona
        nameZona = getIntent().getStringExtra(ListaZonasActivity.ZONA);
        //id de la zona
        String idZona= getIntent().getStringExtra(MainUbungActivity.POSITION);
        try {
            z = (Zona) Singleton.getInstance().darZona(idZona);
        } catch (ParseException e) {
            Log.e("Programación",e.getMessage());
        }
        setTitle(PROGRAMACION + nameZona);
        initListView();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initListView() {
        ListView g = (ListView) findViewById(R.id.listView_programacion);

        g.setAdapter(new ListaProgramacionAdapter(this, z.getId()));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_evento = (TextView) view.findViewById(R.id.evento_id);
                intentDescription(id_evento.getText().toString());
            }
        });

    }

    public void intentDescription(String idEvento) {
        finish();
        Intent t = new Intent(this, DescripcionProgramacionActivity.class);
        t.putExtra(ListaZonasActivity.ZONA, z.getId());
        t.putExtra(MainUbungActivity.POSITION, idEvento);
        startActivity(t);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent t = new Intent(this, ListaZonasActivity.class);
        startActivity(t);
    }


}
