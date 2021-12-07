package com.codpaa.activity;

public class Productos {
    private Integer id;
    private String nombreProducto;
    private String productoCustom;

    public Productos(){

    }

    public Productos(Integer id, String nombreProducto) {
        this.id = id;
        this.nombreProducto = nombreProducto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String toString() {

        this.productoCustom = this.productoCustom = id + " " + nombreProducto;
        return productoCustom;
    }
}
