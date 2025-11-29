package ru.mirea.pasportnikovaeo.bookshell.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.domain.repositories.UserStatsRepository;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private AuthRepository authRepository;
    private UserStatsRepository userStatsRepository;

    public ProfileViewModelFactory(AuthRepository authRepository, UserStatsRepository userStatsRepository) {
        this.authRepository = authRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(authRepository, userStatsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}