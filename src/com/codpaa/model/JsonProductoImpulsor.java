package com.codpaa.model;
/*
 * Created by Grim on 21/05/2017.
 */

import com.codpaa.model.generic.Producto;

import java.util.List;

public class JsonProductoImpulsor {

    private List<Producto> productoList;
    private int idPromotor;

    /**
     *
     * @param productoList lista de productos
     * @param idPromotor id de promotor
     */

    public JsonProductoImpulsor(List<Producto> productoList, int idPromotor) {
        this.productoList = productoList;
        this.idPromotor = idPromotor;
    }


}
