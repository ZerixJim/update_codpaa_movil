package com.codpaa.model;


public class TiendasModel {

    private int _idTienda;
    private String _nombre;
    private String _sucursal;

    public int getIdTienda() {
        return _idTienda;
    }
    public void setIdTienda(int idTienda) {
        this._idTienda = idTienda;
    }
    public String getNombre() {
        return _nombre;
    }
    public void setNombre(String nombre) {
        this._nombre = nombre;
    }
    public String getSucursal() {
        return _sucursal;
    }
    public void setSucursal(String sucursal) {
        this._sucursal = sucursal;
    }
}
