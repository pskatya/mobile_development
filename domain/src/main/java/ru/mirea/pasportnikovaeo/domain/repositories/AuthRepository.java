package ru.mirea.pasportnikovaeo.domain.repositories;

import ru.mirea.pasportnikovaeo.domain.model.User;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);
    void register(String email, String password, String name, AuthCallback callback);
    User getCurrentUser();
    void logout();
    boolean isUserLoggedIn();
    void updateUserName(String userId, String newName, AuthCallback callback);

    interface AuthCallback {
        void onSuccess(User user);
        void onError(Exception e);
    }
}