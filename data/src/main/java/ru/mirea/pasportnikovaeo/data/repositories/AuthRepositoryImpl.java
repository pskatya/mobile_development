package ru.mirea.pasportnikovaeo.data.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserEntity;
import ru.mirea.pasportnikovaeo.data.local.preferences.SharedPrefsManager;
import ru.mirea.pasportnikovaeo.data.remote.firebase.FirebaseAuthSource;
import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.data.model.UserDto;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    private FirebaseAuthSource firebaseAuth;
    private SharedPrefsManager prefs;
    private AppDatabase database;
    private Context context;
    private Handler mainHandler;

    public AuthRepositoryImpl(Context context) {
        this.context = context;
        this.firebaseAuth = new FirebaseAuthSource();
        this.prefs = new SharedPrefsManager(context);
        this.database = AppDatabase.getInstance(context);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        firebaseAuth.login(email, password)
                .addOnSuccessListener(userDto -> {
                    prefs.saveUserData(userDto.getId(), userDto.getEmail(), userDto.getName());

                    UserEntity userEntity = new UserEntity(
                            userDto.getId(),
                            userDto.getEmail(),
                            userDto.getName(),
                            System.currentTimeMillis()
                    );

                    new Thread(() -> {
                        database.userDao().insertUser(userEntity);
                        // Используем mainHandler для callback'а
                        mainHandler.post(() -> callback.onSuccess(userDto.toUser()));
                    }).start();
                })
                .addOnFailureListener(e -> {
                    mainHandler.post(() -> callback.onError(e));
                });
    }

    @Override
    public void register(String email, String password, String name, AuthCallback callback) {
        firebaseAuth.register(email, password, name)
                .addOnSuccessListener(userDto -> {
                    prefs.saveUserData(userDto.getId(), userDto.getEmail(), name);

                    UserEntity userEntity = new UserEntity(
                            userDto.getId(),
                            userDto.getEmail(),
                            name,
                            System.currentTimeMillis()
                    );

                    new Thread(() -> {
                        database.userDao().insertUser(userEntity);
                        mainHandler.post(() -> callback.onSuccess(new User(userDto.getId(), userDto.getEmail(), name)));
                    }).start();
                })
                .addOnFailureListener(e -> {
                    mainHandler.post(() -> callback.onError(e));
                });
    }

    @Override
    public User getCurrentUser() {
        UserDto firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.toUser();
        }

        if (prefs.isLoggedIn()) {
            String userId = prefs.getUserId();
            String email = prefs.getUserEmail();
            String name = prefs.getUserName();

            if (userId != null && email != null) {
                return new User(userId, email, name != null ? name : "User");
            }
        }

        String userId = prefs.getUserId();
        if (userId != null) {
            UserEntity roomUser = database.userDao().getUserById(userId);
            if (roomUser != null) {
                return new User(roomUser.id, roomUser.email, roomUser.name);
            }
        }

        return null;
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null || prefs.isLoggedIn();
    }

    @Override
    public void logout() {
        firebaseAuth.logout();
        prefs.clearUserData();

        String userId = prefs.getUserId();
        if (userId != null) {
            new Thread(() -> {
                database.userDao().deleteUser(userId);
            }).start();
        }
    }

    @Override
    public void updateUserName(String userId, String newName, AuthCallback callback) {
        new Thread(() -> {
            try {
                // 1. Обновляем в Room
                database.userDao().updateUserName(userId, newName);

                // 2. Обновляем в SharedPreferences
                prefs.updateUserName(newName);

                // 3. Обновляем в Firebase
                firebaseAuth.updateUserName(newName)
                        .addOnSuccessListener(aVoid -> {
                            User updatedUser = new User(userId, prefs.getUserEmail(), newName);
                            mainHandler.post(() -> callback.onSuccess(updatedUser));
                        })
                        .addOnFailureListener(e -> {
                            // Если не удалось обновить в Firebase, все равно возвращаем успех
                            // так как данные сохранены локально
                            User updatedUser = new User(userId, prefs.getUserEmail(), newName);
                            mainHandler.post(() -> callback.onSuccess(updatedUser));
                        });

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        }).start();
    }

    public String getStorageInfo() {
        StringBuilder info = new StringBuilder();

        info.append("SharedPreferences: ")
                .append(prefs.isLoggedIn() ? "User logged in" : "No user data")
                .append("\n");

        new Thread(() -> {
            int userCount = database.userDao().getUserCount();
        }).start();

        UserDto firebaseUser = firebaseAuth.getCurrentUser();
        info.append("Firebase Auth: ")
                .append(firebaseUser != null ? "User active" : "No active session");

        return info.toString();
    }
}