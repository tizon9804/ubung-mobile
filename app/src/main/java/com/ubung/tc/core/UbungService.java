package com.ubung.tc.core;

/**
 * Created by Stevenson on 28/02/2015.
 */
public class UbungService {


    private static UbungService ubungservice;

    public static UbungService getInstance(){
        ubungservice= ubungservice!=null?ubungservice:new UbungService();
        return ubungservice;
    }



}
