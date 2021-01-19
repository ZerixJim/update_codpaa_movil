package com.codpaa.model;
/*
 * Created by grim on 26/09/2016.
 */

import com.google.gson.annotations.SerializedName;

public class JsonPhotoUpload {

    @SerializedName("idtienda")
    private int idTienda;
    @SerializedName("idpromo")
    private int idPromotor;
    @SerializedName("idmarca")
    private int idMarca;
    @SerializedName("idex")
    private int idExhibicion;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("dia")
    private int dia;
    @SerializedName("mes")
    private int mes;
    @SerializedName("anio")
    private int anio;
    @SerializedName("evento")
    private int evento;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("productos")
    private int productos[] = null;


    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdExhibicion() {
        return idExhibicion;
    }

    public void setIdExhibicion(int idExhibicion) {
        this.idExhibicion = idExhibicion;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public int getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void convert(String[] array){

        int number[] = new int[array.length];

        for (int i = 0; i < number.length; i++){

            number[i] = Integer.parseInt(array[i]);

        }

        this.productos = number;
    }
}
