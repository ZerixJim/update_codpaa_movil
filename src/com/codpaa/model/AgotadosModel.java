package com.codpaa.model;

public class AgotadosModel {

    private int idTienda;
    private int idPromotor;
    private int idProducto;
    private int estatusProducto;
    private String fecha;

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getEstatusProducto() {
        return estatusProducto;
    }

    public void setEstatusProducto(int estatusProducto) {
        this.estatusProducto = estatusProducto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
