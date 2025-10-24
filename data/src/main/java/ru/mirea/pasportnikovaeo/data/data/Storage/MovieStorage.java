package ru.mirea.pasportnikovaeo.data.data.Storage;

import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;

public interface MovieStorage {
    Movie get();
    boolean save(Movie movie);
}