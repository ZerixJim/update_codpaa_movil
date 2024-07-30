package com.codpaa.model;

public class SpinnerCateProdModel {
    private int idCat;
    private String categoria;

    public int getId(){ return idCat; }
    public String getCategoria(){ return categoria; }

    public void setId(int id){ this.idCat = id; }
    public void setCategoria(String cat){ this.categoria = cat; }
}
