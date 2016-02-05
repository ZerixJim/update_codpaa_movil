package com.codpaa.model;/*
 * Created by Gustavo on 04/02/2016.
 */

public class Pregunta {

    private int idPregunta;
    private int numeroPregunta;
    private String contenidoPregunta;
    private int idRespesta = 0;

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
