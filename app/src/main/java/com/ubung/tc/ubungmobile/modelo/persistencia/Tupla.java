package com.ubung.tc.ubungmobile.modelo.persistencia;

/**
 * Created by cvargasc on 7/03/15.
 */
public class Tupla <Izq, Der> {

    private Izq izq;
    private Der der;

    public Tupla(Izq izq, Der der) {
        this.izq = izq;
        this.der = der;
    }

    public void setIzq(Izq izq) {
        this.izq = izq;
    }

    public Izq getIzq() {
        return izq;
    }

    public void setDer(Der der) {
        this.der = der;
    }

    public Der getDer() {
        return der;
    }
}
