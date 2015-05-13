package com.ubung.tc.ubungmobile.controlador;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventoActivity extends ActionBarActivity {

    private ArrayList<Deporte> deportes;
    private ArrayList<Zona> zonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        setTitle("Crear Evento");
        try {
            initFormat();
        } catch (ParseException e) {
            Log.e("EventoActivity",e.getMessage());
        }
    }

    private void initFormat() throws ParseException {
        zonas = Singleton.getInstance().darZonas();
        deportes = Singleton.getInstance().darDeportes();
        String[] sdeportes = new String[deportes.size()];
        for (int i = 0; i < deportes.size(); i++) {
            sdeportes[i] = deportes.get(i).getNombre();
        }
        String[] szonas = new String[zonas.size()];
        for (int j = 0; j < zonas.size(); j++) {
            szonas[j] = zonas.get(j).getNombre();
        }

        final Spinner spinnerDeportes = (Spinner) findViewById(R.id.deportes_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sdeportes); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeportes.setAdapter(spinnerArrayAdapter);

        final Spinner spinnerZonas = (Spinner) findViewById(R.id.zonas_spinner);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, szonas); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZonas.setAdapter(spinnerArrayAdapter2);


        final EditText date = (EditText) findViewById(R.id.date_picker);
        final EditText hora = (EditText) findViewById(R.id.hour_picker);
        date.setFreezesText(true);
        date.setFocusableInTouchMode(true);
        hora.setFocusableInTouchMode(true);
        Date c = new Date();
        date.setText(c.getDate() + "/" + c.getMonth() + "/" + c.getYear());
        hora.setText(c.getHours() + ":" + c.getMinutes());

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "Hora del evento");

                }
            }
        });

        hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "Fecha del evento");
                }
            }
        });

        Usuario u = Singleton.getInstance().darPropietario();
        TextView usuario = (TextView) findViewById(R.id.organiza_evento);
        usuario.setText(u.getNombreUsuario());

        final Button crear = (Button) findViewById(R.id.button_crear_evento);
        crear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    crear.setTextColor(getResources().getColor(R.color.holo_green_light));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        crear.setTextColor(getResources().getColor(R.color.white));
                        Calendar d = Calendar.getInstance();
                        String t = date.getText().toString();
                        String h = hora.getText().toString();
                        int año = Integer.parseInt(t.split("/")[2]);
                        int mes = Integer.parseInt(t.split("/")[1]);
                        int dia = Integer.parseInt(t.split("/")[0]);
                        int hora = Integer.parseInt(h.split(":")[0]);
                        int min = Integer.parseInt(h.split(":")[1]);
                        d.set(año, mes, dia, hora, min);
                        Date horafecha = new Date();
                        horafecha.setTime(d.getTimeInMillis());
                        String z = zonas.get(spinnerZonas.getSelectedItemPosition()).getId();
                        String dep = deportes.get(spinnerDeportes.getSelectedItemPosition()).getId();
                        Zona zonap = Singleton.getInstance().darZona(z);
                        Deporte sport = Singleton.getInstance().darDeporte(dep);
                        crearEvento(horafecha, zonap, sport);
                    } catch (Exception e) {

                        Toast.makeText(getBaseContext(), "Error en el formato", Toast.LENGTH_LONG).show();
                        Date c = new Date();
                        date.setText(c.getDate() + "/" + c.getMonth() + "/" + c.getYear());
                        hora.setText(c.getHours() + ":" + c.getMinutes());
                    }

                }
                return true;
            }
        });
    }

    private void crearEvento(Date fechaHora, Zona zona, Deporte deporte) {
        try {
            Singleton.getInstance().crearEvento(fechaHora, zona, deporte);
            Toast.makeText(getBaseContext(), "Se ha creado el evento.", Toast.LENGTH_LONG).show();
            //ParsePush.sendMessageInBackground("Se ha crado un evento de " + deporte.getNombre(), ParseInstallation.getQuery());

            nextActivity();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void startAlarm() {

        //PendingIntent pendingIntent= new PendingIntent();

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        //    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //       1000 * 60 * 20, pendingIntent);
    }

    private void nextActivity() {
        finish();
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent t = new Intent(this, LocationActivity.class);
        t.putExtra("last", true);
        startActivity(t);

    }

}
