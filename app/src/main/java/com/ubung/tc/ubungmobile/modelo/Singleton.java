package com.ubung.tc.ubungmobile.modelo;


//import com.ubung.tc.ubungmobile.R;

import com.ubung.tc.ubungmobile.R;

public class Singleton implements InterfazUbung{


    private static Singleton ubungservice;

    public static Singleton getInstance(){
        ubungservice= ubungservice!=null?ubungservice:new Singleton();
        return ubungservice;
    }






    @Override
    public Integer[] getDeportes() {
        //ToDo: crear y retornar las imagenes de deportes;

        Integer[] mThumbIds = {
                R.drawable.ic_launcher, R.drawable.btntest,
                R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.btntest, R.drawable.btntest
        };

        return mThumbIds;
    }
}
