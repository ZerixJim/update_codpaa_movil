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
    private float precioCompra, precioVenta;
    private String fechaPrecio;
    private String nombre;
    private String presentacion;
    private String codeBarras;
    private int estatus;
    private int cantidad;
    private int inventario;

    private int idEstatusCatalogacion;

    private String estatusProceso;

    private boolean checked = false;
    private boolean changes = false;

    private List<String> objeciones = new ArrayList<>();



    public class EstatusTypes{
        public static final int DESCATALOGADO = 1;
        public static final int CATALOGADO = 2;
        public static final int PROCESO_CATALOGACION = 3;
        public static final int ACEPTO_CATALOGACION = 4;
        public static final int PROCESO_CONCRESION = 5;

    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }

    public int getIdEstatusCatalogacion() {
        return idEstatusCatalogacion;
    }

    public void setIdEstatusCatalogacion(int idEstatusCatalogacion) {
        this.idEstatusCatalogacion = idEstatusCatalogacion;
    }

    public String getEstatusProceso() {
        return estatusProceso;
    }

    public void setEstatusProceso(String estatusProceso) {
        this.estatusProceso = estatusProceso;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public int getUtilidad(){

        return Math.round(((precioVenta - precioCompra) / precioCompra) * 100);
    }


    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getFechaPrecio() {
        return fechaPrecio;
    }

    public void setFechaPrecio(String fechaPrecio) {
        this.fechaPrecio = fechaPrecio;
    }

    public boolean isChanges() {
        return changes;
    }

    public void setChanges(boolean changes) {
        this.changes = changes;
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
