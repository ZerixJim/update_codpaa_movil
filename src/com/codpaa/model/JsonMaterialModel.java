package com.codpaa.model;

/*
 * Created by grim on 2/02/17.
 */

import java.util.List;

public class JsonMaterialModel {

    private int idPromotor;

    private List<MaterialModel> materiales;

    public int getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    public List<MaterialModel> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<MaterialModel> materiales) {

        this.materiales = materiales;
    }
}
