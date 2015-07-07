package com.codpaa.models;

/**
 * Created by Gustavo on 24/11/2014.
 */
public class InventarioModel {

    private String marca, producto, tipo;

    private int cantidadSistema, cantidadFisico, status;

    public int getCantidadSistema() {
        return cantidadSistema;
    }

    public void setCantidadSistema(int cantidadSistema) {
        this.cantidadSistema = cantidadSistema;
    }

    public int getCantidadFisico() {
        return cantidadFisico;
    }

    public void setCantidadFisico(int cantidadFisico) {
        this.cantidadFisico = cantidadFisico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
