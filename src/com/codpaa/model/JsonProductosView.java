package com.codpaa.model;
/*
 * Created by grim on 30/08/2016.
 */


import com.google.gson.annotations.Expose;

import java.util.List;

public class JsonProductosView {

    @Expose
    private int idTienda;

    @Expose
    private int idPromotor;

    @Expose
    private String fecha;

    @Expose
    private List<Integer> productos;

    public void setProductos(List<Integer> productos) {
        this.productos = productos;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }


}
