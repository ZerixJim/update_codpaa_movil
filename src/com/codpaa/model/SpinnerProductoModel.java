package com.codpaa.model;

public class SpinnerProductoModel {
	
	private int _idProducto;
	private int _idMarca;
    private String _nombre;
    private String _presentacion;
    private String _codigoBarras;

	private int c1, c2, c3, c4, c5, c6;
	private int f1, f2, f3, f4, f5 ,f6, f7, f8, f9, f10, f11, f12, f13, f14;

	public int getC1() {
		return c1;
	}

	public void setC1(int c1) {
		this.c1 = c1;
	}

	public int getC2() {
		return c2;
	}

	public void setC2(int c2) {
		this.c2 = c2;
	}

	public int getC3() {
		return c3;
	}

	public void setC3(int c3) {
		this.c3 = c3;
	}

	public int getC4() {
		return c4;
	}

	public void setC4(int c4) {
		this.c4 = c4;
	}

	public int getC5() {
		return c5;
	}

	public void setC5(int c5) {
		this.c5 = c5;
	}

	public int getC6() {
		return c6;
	}

	public void setC6(int c6) {
		this.c6 = c6;
	}

	public int getF2() {
		return f2;
	}

	public void setF2(int f2) {
		this.f2 = f2;
	}

	public int getF1() {
		return f1;
	}

	public void setF1(int f1) {
		this.f1 = f1;
	}

	public int getF4() {
		return f4;
	}

	public void setF4(int f4) {
		this.f4 = f4;
	}

	public int getF3() {
		return f3;
	}

	public void setF3(int f3) {
		this.f3 = f3;
	}

	public int getF5() {
		return f5;
	}

	public void setF5(int f5) {
		this.f5 = f5;
	}

	public int getF6() {
		return f6;
	}

	public void setF6(int f6) {
		this.f6 = f6;
	}

	public int getF7() {
		return f7;
	}

	public void setF7(int f7) {
		this.f7 = f7;
	}

	public int getF8() {
		return f8;
	}

	public void setF8(int f8) {
		this.f8 = f8;
	}

	public int getF9() {
		return f9;
	}

	public void setF9(int f9) {
		this.f9 = f9;
	}

	public int getF10() {
		return f10;
	}

	public void setF10(int f10) {
		this.f10 = f10;
	}

	public int getF11() {
		return f11;
	}

	public void setF11(int f11) {
		this.f11 = f11;
	}

	public int getF13() {
		return f13;
	}

	public void setF13(int f13) {
		this.f13 = f13;
	}

	public int getF12() {
		return f12;
	}

	public void setF12(int f12) {
		this.f12 = f12;
	}

	public int getF14() {
		return f14;
	}

	public void setF14(int f14) {
		this.f14 = f14;
	}

    public int getIdMarca() {
        return _idMarca;
    }

    public void setIdMarca(int _idMarca) {
        this._idMarca = _idMarca;
    }



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
