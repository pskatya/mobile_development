package ru.mirea.pasportnikovaeo.movieproject.presentation.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.data.data.Storage.MovieStorage;
import ru.mirea.pasportnikovaeo.data.data.Storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.pasportnikovaeo.data.data.repository.MovieRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
            MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
            return (T) new MainViewModel(movieRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}