package com.codpaa.model.generic;
/*
 * Created by Grim on 20/05/2017.
 */

import java.util.ArrayList;
import java.util.List;

public class Producto {

    private int idProducto;
    private int idMarca;
    private int idTienda;
    private String fecha;
    private float price;
    private String nombre;
    private String presentacion;
    private String codeBarras;
    private int estatus;
    private int cantidad;

    private List<String> objeciones = new ArrayList<>();



    public class EstatusTypes{


        public static final int DESCATALOGADO = 1;

        public static final int CATALOGADO = 2;
        public static final int NO_ACEPTO_CATALOGACION = 3;
        public static final int POR_CATALOGAR = 4;

    }

    public void addOjecion(String element){
        if(!objeciones.contains(element)){

            objeciones.add(element);
        }
    }

    public void removeObjecion(String element){

        if(objeciones.contains(element)){

            objeciones.remove(element);

        }

    }


    public List<String> getObjeciones() {
        return objeciones;
    }

    public void setObjeciones(List<String> objeciones) {
        this.objeciones = objeciones;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int inventario) {
        this.cantidad = inventario;
    }


    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
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
