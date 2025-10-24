package ru.mirea.pasportnikovaeo.bookshell.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.ui.auth.AuthViewModel;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private AuthRepository authRepository;

    public AuthViewModelFactory(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}