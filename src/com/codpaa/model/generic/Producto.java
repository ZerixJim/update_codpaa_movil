package com.codpaa.model.generic;
/*
 * Created by Grim on 20/05/2017.
 */

public class Producto {

    private int idProducto;
    private int idMarca;
    private float price;
    private String nombre;
    private String presentacion;
    private String codeBarras;
    private String estatus;



    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCodeBarras() {
        return codeBarras;
    }

    public void setCodeBarras(String codeBarras) {
        this.codeBarras = codeBarras;
    }
}
