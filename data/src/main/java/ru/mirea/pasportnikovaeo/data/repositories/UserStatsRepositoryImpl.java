package ru.mirea.pasportnikovaeo.data.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserStatsEntity;
import ru.mirea.pasportnikovaeo.domain.model.UserStats;
import ru.mirea.pasportnikovaeo.domain.repositories.UserStatsRepository;

public class UserStatsRepositoryImpl implements UserStatsRepository {
    private AppDatabase database;
    private Handler mainHandler;

    public UserStatsRepositoryImpl(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getUserStats(String userId, UserStatsCallback callback) {
        new Thread(() -> {
            try {
                UserStatsEntity entity = database.userStatsDao().getUserStats(userId);
                if (entity != null) {
                    UserStats stats = new UserStats(
                            entity.userId,
                            entity.booksRead,
                            entity.booksLiked,
                            entity.memberSince
                    );
                    mainHandler.post(() -> callback.onSuccess(stats));
                } else {
                    createUserStats(userId, callback);
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        }).start();
    }

    @Override
    public void updateBooksRead(String userId, int booksRead, UpdateCallback callback) {
        new Thread(() -> {
            try {
                database.userStatsDao().updateBooksRead(userId, booksRead, System.currentTimeMillis());
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        }).start();
    }

    @Override
    public void updateBooksLiked(String userId, int booksLiked, UpdateCallback callback) {
        new Thread(() -> {
            try {
                database.userStatsDao().updateBooksLiked(userId, booksLiked, System.currentTimeMillis());
                mainHandler.post(callback::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        }).start();
    }

    @Override
    public void createUserStats(String userId, UserStatsCallback callback) {
        new Thread(() -> {
            try {
                UserStatsEntity entity = new UserStatsEntity(
                        userId,
                        0, // booksRead
                        0, // booksLiked
                        System.currentTimeMillis(), // memberSince
                        System.currentTimeMillis() // lastUpdated
                );
                database.userStatsDao().insertUserStats(entity);

                UserStats stats = new UserStats(userId, 0, 0, System.currentTimeMillis());
                mainHandler.post(() -> callback.onSuccess(stats));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        }).start();
    }
}