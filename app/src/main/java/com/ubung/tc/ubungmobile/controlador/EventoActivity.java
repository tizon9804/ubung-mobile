package com.ubung.tc.ubungmobile.controlador;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventoActivity extends ActionBarActivity {

    private ArrayList<Deporte> deprotes;
    private ArrayList<Zona> zonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        initFormat();
    }

    private void initFormat() {
        zonas= Singleton.getInstance().darZonas();
        deprotes= Singleton.getInstance().darDeportes();

    }

    private void crearEvento(Date fechaHora,Zona zona,Deporte deporte){
        Usuario organizador= Singleton.getInstance().darPropietario();
        try {
            Singleton.getInstance().crearEvento(fechaHora,zona,deporte,organizador);
        } catch (ExcepcionPersistencia excepcionPersistencia) {
            excepcionPersistencia.printStackTrace();
            Toast.makeText(getBaseContext(), "Hubo un problema al Crear el evento ", Toast.LENGTH_LONG).show();
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

}
