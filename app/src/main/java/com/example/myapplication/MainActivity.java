package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.MovieDAO;
import com.example.myapplication.model.MovieDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> movieList;
    private SearchView searchView;
    private MovieDAO movieDAO;
    private MovieDatabase movieDatabase;



    private MovieAdapter movieAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDatabase = MovieDatabase.getINSTANCE(getApplicationContext());
        movieDAO = movieDatabase.getMovieDAO();
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        movieList = new ArrayList<>();
        fetchMovies();
    }

    private void filterList(String text) {
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movieList){
            if (movie.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(movie);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            movieAdapter.setFilteredList(filteredList);
        }
    }




    private void fetchMovies() {

        String url = "https://api.npoint.io/95be87af7dab2058f17d";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String overview = jsonObject.getString("overview");
                                String poster = jsonObject.getString("poster");
                                Double rating = jsonObject.getDouble("rating");

                                Movie movie = new Movie(title , poster , overview , rating);
                                saveMovieToDatabase(movie);
                                movieList.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                             movieAdapter  = new MovieAdapter(MainActivity.this , movieList);

                            recyclerView.setAdapter(movieAdapter);
                            checkDatabase();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
    private void saveMovieToDatabase(final Movie movie) {
        // Use a background thread or AsyncTask to perform database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieDAO.insertMovie(movie);

                    // Check the database after insertion
                    checkDatabase();

                    showDataSavedMessage("Data saved in database");
                } catch (Exception e) {
                    e.printStackTrace();
                    // Log an error message if the insertion fails
                    Log.e("MainActivity", "Error saving movie to database: " + e.getMessage());
                }
            }
        }).start();
    }
    private void checkDatabase() {
        // Use a background thread or AsyncTask to perform database query
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Movie> savedMovies = movieDAO.getAllMovie().getValue();

                if (savedMovies != null && !savedMovies.isEmpty()) {
                    // Log or display the saved movies
                    for (Movie movie : savedMovies) {
                        Log.d("MainActivity", "Saved Movie : " + movie.getTitle());
                    }
                }
            }
        }).start();
    }
    private void showDataSavedMessage(final String message) {
        // Use the main (UI) thread to show the Toast
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}