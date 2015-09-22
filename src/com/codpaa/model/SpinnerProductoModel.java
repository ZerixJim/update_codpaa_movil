package com.codpaa.model;

public class SpinnerProductoModel {
	
	private int _idProducto;
	private String _nombre;
	private String _presentacion;
	private String _codigoBarras;

    public String getCodigoBarras() {
        return _codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this._codigoBarras = codigoBarras;
    }

    public void setIdProducto(int idProductos){
		this._idProducto = idProductos;
	}
	
	public void setNombre(String nombre){
		this._nombre = nombre;
	}
	
	public void setPresentacion(String presentacion){
		this._presentacion = presentacion;
	}
	
	public int getIdProducto(){
		return this._idProducto;
	}
	public String getNombre(){
		return this._nombre;
	}
	
	public String getPresentacion(){
		return this._presentacion;
	}

}
