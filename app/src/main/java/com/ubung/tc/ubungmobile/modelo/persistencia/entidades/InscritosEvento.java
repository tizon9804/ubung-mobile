package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.database.Cursor;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.persistencia.Tupla;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 7/03/15.
 */
public class InscritosEvento {
    private static final String LOG_NAME = "InscrEvent";

    private ManejadorPersistencia manejadorPersistencia;
    private Boolean contengoUsuarios;

    private ArrayList<Tupla<Long, Date>> inscritosEvento;

    /**
     * Devuelve todos los usuarios inscritos al evento pasado como parámetro
     * @param evento el evento para el cual se quieren conocer los usuarios inscritos
     */
    protected InscritosEvento(Evento evento, ManejadorPersistencia manejadorPersistencia) {
        this.manejadorPersistencia = manejadorPersistencia;
        this.contengoUsuarios = true;

        String consulta = "SELECT "+ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_IDINSCRITO+","+
                ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_FECHAINSCRIP+" FROM "+ManejadorPersistencia.TABLA_INSCRITOSEVENTO+
                " WHERE "+ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_IDEVENTO+"="+evento.getId();

        procesarConsulta(manejadorPersistencia.consultaPlana(consulta));
    }

    /**
     * Devuelve todos los eventos a los cuales se ha inscrito el usuario pasado como parámetro
     * @param usuario el usuario para el cual se quieren conocer los eventos a losi cuales se ha
     *                inscrito.
     */
    protected InscritosEvento(Usuario usuario, ManejadorPersistencia manejadorPersistencia) {
        this.manejadorPersistencia = manejadorPersistencia;
        this.contengoUsuarios = false;

        String consulta = "SELECT "+ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_IDEVENTO+","+
                ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_FECHAINSCRIP+" FROM "+ManejadorPersistencia.TABLA_INSCRITOSEVENTO+
                " WHERE "+ManejadorPersistencia.CAMPO_INSCRITOSEVENTO_IDINSCRITO+"="+usuario.getId();

        procesarConsulta(manejadorPersistencia.consultaPlana(consulta));
    }

    private void procesarConsulta(Cursor cursor) {
        inscritosEvento = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Date fechaInscripcion = null;
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fechaInscripcion = dateFormat.parse(cursor.getString(1));
                } catch (ParseException e) {
                    Log.e(LOG_NAME + "procesarCons", "No fue posible convertir string to date para el campo fechaInscripcion");
                }
                inscritosEvento.add(new Tupla<>(cursor.getLong(0), fechaInscripcion));
            } while (cursor.moveToNext());
        }
    }

    protected ArrayList<Tupla<Usuario, Date>> getInscritosEvento() {
        if (!contengoUsuarios) {
            Log.e(LOG_NAME+"getInscEve", "Está intentando obtener usuarios desde un arreglo de eventos!!");
            return null;
        }

        ArrayList<Tupla<Usuario, Date>> respuesta = new ArrayList<>();
        for(Tupla t : inscritosEvento) {
            Usuario usuario = manejadorPersistencia.darUsuario((long) t.getIzq());
            respuesta.add(new Tupla<>(usuario, (Date) t.getDer()));
        }

        return respuesta;
    }

    protected ArrayList<Tupla<Evento, Date>> getEventosUsuario() {
        if (contengoUsuarios) {
            Log.e(LOG_NAME+"getInscEve", "Está intentando obtener eventos desde un arreglo de usuarios!!");
            return null;
        }

        ArrayList<Tupla<Evento, Date>> respuesta = new ArrayList<>();
        for(Tupla t : inscritosEvento) {
            Evento evento = manejadorPersistencia.darEvento((long) t.getIzq());
            respuesta.add(new Tupla<>(evento, (Date) t.getDer()));
        }

        return respuesta;
    }
}
