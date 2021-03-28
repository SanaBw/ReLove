package com.sanida.relove.models;

public class Shop {
    String name;
    String price;
    String imgpath;
    String description;

    public Shop(String name, String price, String imgpath, String description) {
        this.name = name;
        this.price = price;
        this.imgpath = imgpath;
        this.description = description;
    }



    public Shop(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgPath() {
        return imgpath;
    }

    public void setImgPath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





}
