package ru.mirea.pasportnikovaeo.movieproject.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.pasportnikovaeo.movieproject.R;
import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.data.data.Storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.pasportnikovaeo.data.data.repository.MovieRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;
import ru.mirea.pasportnikovaeo.domain.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.pasportnikovaeo.domain.domain.usecases.SaveFilmToFavoriteUseCase;

public class MainActivity extends AppCompatActivity {

    private MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
        movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);

        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieName = text.getText().toString();
                if (!movieName.isEmpty()) {
                    Movie movie = new Movie(2, movieName);
                    Boolean result = new SaveFilmToFavoriteUseCase(movieRepository).execute(movie);
                    textView.setText(String.format("Фильм '%s' сохранен: %s", movieName, result ? "успешно" : "ошибка"));
                } else {
                    textView.setText("Введите название фильма!");
                }
            }
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Ваш любимый фильм: %s", movie.getName()));
            }
        });
    }
}