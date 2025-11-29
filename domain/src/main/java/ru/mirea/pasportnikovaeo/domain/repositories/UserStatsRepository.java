package ru.mirea.pasportnikovaeo.domain.repositories;

import ru.mirea.pasportnikovaeo.domain.model.UserStats;

public interface UserStatsRepository {
    void getUserStats(String userId, UserStatsCallback callback);
    void updateBooksRead(String userId, int booksRead, UpdateCallback callback);
    void updateBooksLiked(String userId, int booksLiked, UpdateCallback callback);
    void createUserStats(String userId, UserStatsCallback callback);

    interface UserStatsCallback {
        void onSuccess(UserStats userStats);
        void onError(Exception e);
    }

    interface UpdateCallback {
        void onSuccess();
        void onError(Exception e);
    }
}