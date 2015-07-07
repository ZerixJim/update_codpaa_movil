package com.codpaa.models;

public class SpinnerMarcaModel {
	private int _id;
	private String _nombre;
	
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

}
