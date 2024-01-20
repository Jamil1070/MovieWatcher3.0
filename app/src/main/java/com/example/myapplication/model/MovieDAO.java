package com.example.myapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {
    @Insert
    void insertMovie(Movie m);

    @Query("SELECT * FROM Movie ORDER BY title")
    LiveData<List<Movie>> getAllMovie();

    @Query("SELECT * FROM Movie ORDER BY rating")
    LiveData<List<Movie>> getMovieByRating();
    @Update
    void updateMovie(Movie m);
    @Delete
    void deleteMovie(Movie m);

}
