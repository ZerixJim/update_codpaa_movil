package com.codpaa.model;

public class SliderData {

    private String title, text;
    private int imgResId;

    public SliderData(int imgResId, String title, String text){
        this.imgResId = imgResId;
        this.title = title;
        this.text = text;
    }

    public int getImgResId(){ return imgResId; }


    public String getTitle(){ return title; }

    public void setText(String text){ this.text = text; }

    public String getText(){ return text; }
}
