package com.example.myapplication.model;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

@PrimaryKey(autoGenerate = true)
    public int id;
    private String title , poster , overview;
    private Double rating;
    public Movie(){

    }
@Ignore
    public Movie(String title , String poster , String overview , Double rating){
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
