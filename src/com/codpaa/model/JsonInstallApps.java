package com.codpaa.model;

/*
 * Created by grim on 03/04/2017.
 */


import java.util.List;

public class JsonInstallApps {


    private int idPromotor;
    private String fecha;
    private String imei;
    private List<Application> gApps;


    public List<Application> getGoogleApplication() {
        return gApps;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setGoogleApplication(List<Application> googleApplication) {
        this.gApps = googleApplication;
    }

    public int getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    public String getFecha() {
        return fecha;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;

    }


}
