package ru.mirea.pasportnikovaeo.domain.usecase;

import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class RegisterUseCase {
    private AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, String name, AuthRepository.AuthCallback callback) {
        authRepository.register(email, password, name, callback);
    }
}