package com.codpaa.model;


public class MarcaModel {
    private int _id;
    private String _nombre;
    private String url;

    public void setId(int id){
        this._id = id;
    }

    public void setNombre(String nombre){
        this._nombre = nombre;
    }

    public int getId(){
        return this._id;
    }

    public String getNombre(){
        return this._nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
