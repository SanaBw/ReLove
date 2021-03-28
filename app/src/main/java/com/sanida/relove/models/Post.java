package com.sanida.relove.models;

public class Post {

    private String title;
    private String text;



    private String imgpath;

    public Post(){

    }
    public Post(String text, String title, String imgpath) {
        this.title = title;
        this.text = text;
        this.imgpath=imgpath;
    }

    public String getImgPath() {
        return imgpath;
    }

    public void setImgPath(String imgpath) {
        this.imgpath = imgpath;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }







}
