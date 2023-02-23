package com.example.eventory.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class CardModel implements Serializable {

    private String name;
    private String date_time;
    private String place;
    private String img_url;
    private String min_price;
    private String description;
    private String duration;
    private String genre;
    private String min_age;
    private ArrayList<String> tags;
    private ArrayList<String> more_images;
    private ArrayList<String> date_time_list;
    private String link;

    public CardModel(String name, String date_time, String place, String img_url, String min_price, String description, String duration, String genre, String min_age, ArrayList<String> tags, ArrayList<String> more_images, ArrayList<String> date_time_list, String link) {
        this.name = name;
        this.date_time = date_time;
        this.place = place;
        this.img_url = img_url;
        this.min_price = min_price;
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.min_age = min_age;
        this.tags = tags;
        this.more_images = more_images;
        this.date_time_list = date_time_list;
        this.link = link;
    }



    private boolean liked = false;

   /* public CardModel(String name, String date_time, String place, String img_url, String min_price, String description, String duration, String genre, String min_age, String tags) {
        this.name = name;
        this.date_time = date_time;
        this.place = place;
        this.img_url = img_url;
        this.min_price = min_price;
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.min_age = min_age;
        this.tags = tags;
    }*/

    public CardModel(){}


    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }



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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMin_age() {
        return min_age;
    }

    public void setMin_age(String min_age) {
        this.min_age = min_age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getMore_images() {
        return more_images;
    }

    public void setMore_images(ArrayList<String> more_images) {
        this.more_images = more_images;
    }

    public ArrayList<String> getDate_time_list() {
        return date_time_list;
    }

    public void setDate_time_list(ArrayList<String> date_time_list) {
        this.date_time_list = date_time_list;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
