package com.ubung.tc.ubungmobile.modelo.persistencia;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 1/03/15.
 */
public class Evento {

    private int id;
    private Date fecha;
    private Zona zona;
    private Deporte deporte;
    private Usuario organizador;
    private ArrayList<Usuario> inscritos;

}
