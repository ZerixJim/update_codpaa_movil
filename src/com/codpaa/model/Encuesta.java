package com.codpaa.model;
/*
 * Created by grim on 3/02/16.
 */

public class Encuesta {

    private int idEncuesta;
    private String nombreEncuesta;
    private String nombreMarca;
    private int tipoEncuesta;

    public int getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(int tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }
}
