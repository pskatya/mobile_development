package ru.mirea.pasportnikovaeo.movieproject.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;
import ru.mirea.pasportnikovaeo.domain.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.pasportnikovaeo.domain.domain.usecases.SaveFilmToFavoriteUseCase;

public class MainViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<String> movieResult = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MutableLiveData<String> getMovieResult() {
        return movieResult;
    }

    public void saveMovie(String movieName) {
        if (movieName != null && !movieName.isEmpty()) {
            Movie movie = new Movie(2, movieName);
            Boolean result = new SaveFilmToFavoriteUseCase(movieRepository).execute(movie);
            if (result) {
                movieResult.setValue("Фильм '" + movieName + "' сохранен успешно!");
            } else {
                movieResult.setValue("Ошибка сохранения фильма");
            }
        } else {
            movieResult.setValue("Введите название фильма!");
        }
    }

    public void getMovie() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        movieResult.setValue("Ваш любимый фильм: " + movie.getName());
    }
}