package com.codpaa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Created by Gustavo Ramón Ibarra Maciel on 04/12/2014.
 */
public class ProductosModel {

    @Expose
    private int idProducto;

    private int idMarca;
    private String nombre;
    private String presentacion;
    private boolean seleted = false;

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public boolean isSeleted() {
        return seleted;
    }

    public void setSeleted(boolean seleted) {
        this.seleted = seleted;
    }

    public void setIdProducto(int idProductos){
        this.idProducto = idProductos;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setPresentacion(String presentacion){
        this.presentacion = presentacion;
    }

    public int getIdProducto(){
        return this.idProducto;
    }
    public String getNombre(){
        return this.nombre;
    }

    public String getPresentacion(){
        return this.presentacion;
    }
}
