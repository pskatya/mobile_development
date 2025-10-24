package ru.mirea.pasportnikovaeo.data.data.Storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;

public class SharedPrefMovieStorage implements MovieStorage {
    private static final String SHARED_PREFS_NAME = "movie_prefs";
    private static final String KEY_MOVIE_NAME = "movie_name";
    private static final String KEY_MOVIE_ID = "movie_id";

    private SharedPreferences sharedPreferences;

    public SharedPrefMovieStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Movie get() {
        String movieName = sharedPreferences.getString(KEY_MOVIE_NAME, "unknown");
        int movieId = sharedPreferences.getInt(KEY_MOVIE_ID, 1);
        return new Movie(movieId, movieName);
    }

    @Override
    public boolean save(Movie movie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOVIE_NAME, movie.getName());
        editor.putInt(KEY_MOVIE_ID, movie.getId());
        return editor.commit();
    }
}