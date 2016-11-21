package com.codpaa.model;/*
 * Created by Gustavo on 04/02/2016.
 */

public class Pregunta {

    private int idPregunta;
    private int idEncuesta;
    private int numeroPregunta;
    private int idTipo;
    private String contenidoPregunta;
    private String respuesta = "";
    private boolean isResponded = false;

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    private int idRespesta = 0;

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public boolean isResponded() {
        return isResponded;
    }

    public void setResponded(boolean responded) {
        isResponded = responded;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getNumeroPregunta() {
        return numeroPregunta;
    }

    public void setNumeroPregunta(int numeroPregunta) {
        this.numeroPregunta = numeroPregunta;
    }



    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getContenidoPregunta() {
        return contenidoPregunta;
    }

    public void setContenidoPregunta(String contenidoPregunta) {
        this.contenidoPregunta = contenidoPregunta;
    }

    public int getIdRespesta() {
        return idRespesta;
    }

    public void setIdRespesta(int idRespesta) {
        this.idRespesta = idRespesta;
    }
}
