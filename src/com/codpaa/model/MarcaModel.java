package com.codpaa.model;


public class MarcaModel {
    private int _id;
    private String _nombre;
    private String url;
    private int numbrePhotos, numberFrentes;


    public int getNumberFrentes() {
        return numberFrentes;
    }

    public void setNumberFrentes(int numberFrentes) {
        this.numberFrentes = numberFrentes;
    }

    public int getNumbrePhotos() {
        return numbrePhotos;
    }

    public void setNumbrePhotos(int numbrePhotos) {
        this.numbrePhotos = numbrePhotos;
    }

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
