package com.example.eventory.models;

import java.io.Serializable;

public class CardModel implements Serializable {

    private String name;
    private String date_time;
    private String place;
    private String img_url;
    private String min_price;
    private boolean liked = false;

    public CardModel(String name, String date_time, String place, String img_url, String min_price) {
        this.name = name;
        this.date_time = date_time;
        this.place = place;
        this.img_url = img_url;
        this.min_price = min_price;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public CardModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }


}
