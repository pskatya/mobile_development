package ru.mirea.pasportnikovaeo.domain.domain.usecases;
import ru.mirea.pasportnikovaeo.domain.domain.models.Movie;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}
