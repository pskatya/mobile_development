package ru.mirea.pasportnikovaeo.data.data.Storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.data.data.Storage.models.Movie;

public class SharedPrefMovieStorage implements MovieStorage {
    private static final String SHARED_PREFS_NAME = "shared_prefs_name";
    private static final String KEY = "movie_name";
    private static final String DATE_KEY = "movie_date";
    private static final String ID_KEY = "movie_id";
    private SharedPreferences sharedPreferences;
    private Context context;
    public SharedPrefMovieStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
    @Override
    public Movie get() {
        String movieName = sharedPreferences.getString(KEY, "unknown");
        String movieDate = sharedPreferences.getString(DATE_KEY,
                String.valueOf(LocalDate.now()));
        int movieId = sharedPreferences.getInt(ID_KEY, -1);
        return new Movie(movieId, movieName, movieDate);
    }
    @Override
    public boolean save(Movie movie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, movie.getName());
        editor.putString(DATE_KEY, String.valueOf(LocalDate.now()));
        editor.putInt(ID_KEY, movie.getId()); // Используйте movie.getId() вместо 1
        return editor.commit(); // commit() возвращает boolean
    }
}
