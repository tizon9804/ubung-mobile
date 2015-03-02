package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.persistencia.Deporte;

public class Singleton implements InterfazUbung {


    private static Singleton ubungservice;

    public static Singleton getInstance() {
        ubungservice = ubungservice != null ? ubungservice : new Singleton();
        return ubungservice;
    }


    @Override
    public Integer[] getDeportes() {
        //ToDo: crear y retornar las imagenes de deportes;

        Integer[] mThumbIds = {
                R.drawable.basket,  R.drawable.futbol,
                R.drawable.basket,  R.drawable.futbol,
                R.drawable.voleibol,  R.drawable.voleibol
        };

        return mThumbIds;
    }

    @Override
    public Deporte getDeporte(int pos) {
        return null;
    }
}
