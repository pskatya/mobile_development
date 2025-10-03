package ru.mirea.pasportnikovaeo.data.data.Storage;

import ru.mirea.pasportnikovaeo.data.data.Storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}
