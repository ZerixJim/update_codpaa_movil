package com.codpaa.model;

/*
 * Created by Gustavo on 02/10/2014.
 */
public class FrentesModel {
    private String producto;
    private String marca;
    private int cantidad;
    private int status;
    private String fecha;
    private int filas;
    private int idCategoria;

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProducto() {return producto;}

    public void setProducto(String producto) {this.producto = producto;}

    public String getMarca() {return marca;}

    public void setMarca(String marca) {this.marca = marca;}

    public int getCantidad() {return cantidad;}

    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}

    public int getCategoria() { return idCategoria; }

    public void setCategoria(int cate) {this.idCategoria = cate;}
}
