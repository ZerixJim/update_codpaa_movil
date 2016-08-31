package com.codpaa.model;
/*
 * Created by grim on 30/08/2016.
 */


import com.google.gson.annotations.Expose;


public class JsonProductosView {

    @Expose
    private int idTienda;

    @Expose
    private int idPromotor;

    @Expose
    private String fecha;

    @Expose
    private int productos[] = null;

    public void setProductos(int[] productos) {
        this.productos = productos;
    }



    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }


    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }


    public void convert(String[] array){

        int number[] = new int[array.length];

        for (int i = 0; i < number.length; i++){

            number[i] = Integer.parseInt(array[i]);

        }

        this.productos = number;
    }


}
