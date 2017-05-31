package com.codpaa.model;
/*
 * Created by Grim on 22/05/2017.
 */

import java.util.List;

public class JsonUpdateFirma {

    private int idPromotor;


    private List<AvanceGestionModel> list;

    public JsonUpdateFirma(int idPromotor, List<AvanceGestionModel> list) {
        this.idPromotor = idPromotor;
        this.list = list;
    }
}
