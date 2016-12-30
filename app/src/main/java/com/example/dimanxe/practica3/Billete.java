package com.example.dimanxe.practica3;

/**
 * Created by dimanxe on 29/12/2016.
 */

public class Billete {
    private String Origen;
    private String Destino;
    private String fecha;
    private int Nplazas;
    private int idbillete;

    public Billete(){

    }

    public String getOrigen() {
        return Origen;
    }
    public void setOrigen(String origen) {
        Origen = origen;
    }
    public String getDestino() {
        return Destino;
    }
    public void setDestino(String destino) {
        Destino = destino;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public int getNplazas() {
        return Nplazas;
    }
    public void setNplazas(int nplazas) {
        Nplazas = nplazas;
    }

    public int getIdbillete() {
        return idbillete;
    }

    public void setIdbillete(int idbillete) {
        this.idbillete = idbillete;
    }
}
