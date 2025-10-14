package ru.mirea.pasportnikovaeo.domain.usecase;

import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class LoginUseCase {
    private AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.login(email, password, callback);
    }

    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }
}