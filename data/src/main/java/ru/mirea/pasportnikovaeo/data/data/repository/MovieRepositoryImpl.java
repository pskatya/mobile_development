package ru.mirea.pasportnikovaeo.data.data.repository;

import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        return movieStorage.save(movie);
    }

    @Override
    public Movie getMovie() {
        return movieStorage.get();
    }
}