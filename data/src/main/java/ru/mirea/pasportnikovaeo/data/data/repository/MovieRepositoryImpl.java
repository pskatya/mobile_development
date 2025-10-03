package ru.mirea.pasportnikovaeo.data.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private static final String SHARED_PREFS_NAME = "shared_prefs_name";
    private static final String KEY = "movie_name";
    private final MovieStorage movieStorage;
    private SharedPreferences sharedPreferences;
    private Context context;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean saveMovie(Movie movie){
        sharedPreferences.edit().putString(KEY, movie.getName()).commit();
        return true;
    }

    @Override
    public Movie getMovie(){
        String movie_name = sharedPreferences.getString(KEY, "unknown");
        return new Movie(1, movie_name);
    }
}