package com.ubung.tc.ubungmobile.modelo;


//import com.ubung.tc.ubungmobile.R;

import com.ubung.tc.ubungmobile.R;

public class Ubung {


    private static Ubung ubungservice;

    public static Ubung getInstance(){
        ubungservice= ubungservice!=null?ubungservice:new Ubung();
        return ubungservice;
    }


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
