package ru.mirea.pasportnikovaeo.movieproject.domain.repository;

import ru.mirea.pasportnikovaeo.movieproject.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
