package ru.mirea.pasportnikovaeo.movieproject.data.repository;

import ru.mirea.pasportnikovaeo.movieproject.domain.models.Movie;
import ru.mirea.pasportnikovaeo.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private Movie savedMovie; // Добавляем поле для хранения фильма

    public MovieRepositoryImpl() {
        // Пустой конструктор
    }

    @Override
    public boolean saveMovie(Movie movie) {
        if (movie != null && !movie.getName().isEmpty()) {
            this.savedMovie = movie; // Сохраняем фильм
            return true;
        }
        return false;
    }

    @Override
    public Movie getMovie() {
        // Возвращаем сохраненный фильм, если есть, иначе дефолтный
        if (savedMovie != null) {
            return savedMovie;
        } else {
            return new Movie(1, "Game of thrones");
        }
    }
}