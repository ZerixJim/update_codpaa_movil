package com.codpaa.activity;

public class Marcas {
    private Integer id;
    private String nombre;
    private String marcaCustom;


    public Marcas(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Marcas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        this.marcaCustom = this.marcaCustom = this.id + " " + this.nombre;

        return marcaCustom;
    }
}
