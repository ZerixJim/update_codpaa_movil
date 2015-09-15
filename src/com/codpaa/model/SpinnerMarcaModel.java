package com.codpaa.model;

public class SpinnerMarcaModel {
	private int _id;
	private String _nombre;
	private String descrip;

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    private String imgUrl;
	
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
