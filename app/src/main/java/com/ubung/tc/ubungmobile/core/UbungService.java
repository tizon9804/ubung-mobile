package com.ubung.tc.ubungmobile.core;


//import com.ubung.tc.ubungmobile.R;

import com.ubung.tc.ubungmobile.R;

public class UbungService {


    private static UbungService ubungservice;

    public static UbungService getInstance(){
        ubungservice= ubungservice!=null?ubungservice:new UbungService();
        return ubungservice;
    }


    public Integer[] getDeportes() {
        //ToDo: crear y retornar las imagenes de deportes;

  Integer[] mThumbIds = {
              R.drawable.ic_launcher, R.drawable.ic_launcher,
               R.drawable.ic_launcher, R.drawable.ic_launcher,
               R.drawable.ic_launcher, R.drawable.ic_launcher
       };

       return mThumbIds;

    }
}
