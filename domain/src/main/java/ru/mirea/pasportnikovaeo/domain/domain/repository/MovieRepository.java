package ru.mirea.pasportnikovaeo.domain.domain.repository;

import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
