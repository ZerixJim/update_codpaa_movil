package com.codpaa.model;/*
 * Created by Gustavo on 29/03/2016.
 */

public class MensajeModel {

    private String asunto;
    private String mensaje;
    private String dateTime;
    private boolean visto = false;
    private int idMensaje;

    public int getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(int idServidor) {
        this.idServidor = idServidor;
    }

    private int idServidor;

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }
}
