package com.example.myapplication.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {Movie.class})
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase INSTANCE;

    public static MovieDatabase getINSTANCE(Context context){
        if (INSTANCE == null){

            INSTANCE = Room.databaseBuilder(context, MovieDatabase.class,"movie.sqlite").build();
        }
        return INSTANCE;
    }
   public abstract MovieDAO getMovieDAO();


}
