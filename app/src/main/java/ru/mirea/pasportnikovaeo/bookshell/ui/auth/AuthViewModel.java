package ru.mirea.pasportnikovaeo.bookshell.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.domain.usecase.LoginUseCase;
import ru.mirea.pasportnikovaeo.domain.usecase.RegisterUseCase;

public class AuthViewModel extends ViewModel {
    private LoginUseCase loginUseCase;
    private RegisterUseCase registerUseCase;
    private AuthRepository authRepository;

    // LiveData для UI состояния
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<String> authError = new MutableLiveData<>();
    private MutableLiveData<Boolean> authSuccess = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>();

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        this.loginUseCase = new LoginUseCase(authRepository);
        this.registerUseCase = new RegisterUseCase(authRepository);

        // Проверяем статус авторизации при создании ViewModel
        checkUserLoginStatus();
    }

    private void checkUserLoginStatus() {
        boolean loggedIn = authRepository.isUserLoggedIn();
        isUserLoggedIn.setValue(loggedIn);

        if (loggedIn) {
            // Загружаем данные текущего пользователя
            User user = authRepository.getCurrentUser();
            if (user != null) {
                currentUser.setValue(user);
            }
        }
    }

    public void login(String email, String password) {
        isLoading.setValue(true);
        authError.setValue(null);

        loginUseCase.execute(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.postValue(false);
                currentUser.postValue(user);
                authSuccess.postValue(true);
                isUserLoggedIn.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                authError.postValue("Login failed: " + e.getMessage());
            }
        });
    }

    public void register(String email, String password, String name) {
        isLoading.setValue(true);
        authError.setValue(null);

        registerUseCase.execute(email, password, name, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.postValue(false);
                currentUser.postValue(user);
                authSuccess.postValue(true);
                isUserLoggedIn.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                authError.postValue("Registration failed: " + e.getMessage());
            }
        });
    }

    // ✅ ДОБАВЛЕН МЕТОД LOGOUT
    public void logout() {
        isLoading.setValue(true);

        // Выполняем выход в фоновом потоке
        new Thread(() -> {
            try {
                // Вызываем logout в репозитории
                authRepository.logout();

                // Обновляем UI в основном потоке
                isLoading.postValue(false);
                currentUser.postValue(null);
                authSuccess.postValue(false);
                isUserLoggedIn.postValue(false);
                authError.postValue(null);

            } catch (Exception e) {
                isLoading.postValue(false);
                authError.postValue("Logout failed: " + e.getMessage());
            }
        }).start();
    }

    // ✅ ДОБАВЛЕН МЕТОД ДЛЯ ОБНОВЛЕНИЯ ДАННЫХ ПОЛЬЗОВАТЕЛЯ
    public void refreshUserData() {
        User user = authRepository.getCurrentUser();
        if (user != null) {
            currentUser.setValue(user);
        }
    }

    // LiveData геттеры
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getAuthError() {
        return authError;
    }

    public LiveData<Boolean> getAuthSuccess() {
        return authSuccess;
    }

    public LiveData<Boolean> getIsUserLoggedIn() {
        return isUserLoggedIn;
    }
}