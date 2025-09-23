package ru.mirea.pasportnikovaeo.movieproject.domain.usecases;

import ru.mirea.pasportnikovaeo.movieproject.domain.models.Movie;
import ru.mirea.pasportnikovaeo.movieproject.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;
    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}

