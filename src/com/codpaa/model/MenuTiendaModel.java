package com.codpaa.model;

/*
 * Created by grim on 24/01/17.
 */

public class MenuTiendaModel {

    private int idMenu;
    private String nombreMenu;
    private String image;
    private String subDescription;


    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }
}
