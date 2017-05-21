package com.codpaa.model.generic;
/*
 * Created by Grim on 20/05/2017.
 */

public class Producto {

    private int idProducto;
    private int idMarca;
    private int idTienda;
    private String fecha;
    private float price;
    private String nombre;
    private String presentacion;
    private String codeBarras;
    private String estatus = null;
    private int inventario;



    public class EstatusTypes{

        public static final String CATALOGADO = "catalogado";
        public static final String POR_CATALOGAR = "por_catalogar";

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }


    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }


    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCodeBarras() {
        return codeBarras;
    }

    public void setCodeBarras(String codeBarras) {
        this.codeBarras = codeBarras;
    }
}
