package ru.mirea.pasportnikovaeo.data.repositories;

import android.content.Context;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserEntity;
import ru.mirea.pasportnikovaeo.data.local.preferences.SharedPrefsManager;
import ru.mirea.pasportnikovaeo.data.remote.firebase.FirebaseAuthSource;
import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.data.model.UserDto;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    private FirebaseAuthSource firebaseAuth; // Способ 1: Firebase (сеть)
    private SharedPrefsManager prefs;        // Способ 2: SharedPreferences
    private AppDatabase database;            // Способ 3: Room Database
    private Context context;

    public AuthRepositoryImpl(Context context) {
        this.context = context;
        this.firebaseAuth = new FirebaseAuthSource();
        this.prefs = new SharedPrefsManager(context);
        this.database = AppDatabase.getInstance(context);
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        // Способ 1: Firebase Auth (сеть)
        firebaseAuth.login(email, password)
                .addOnSuccessListener(userDto -> {
                    // Способ 2: Сохраняем в SharedPreferences
                    prefs.saveUserData(userDto.getId(), userDto.getEmail(), userDto.getName());

                    // Способ 3: Сохраняем в Room Database
                    UserEntity userEntity = new UserEntity(
                            userDto.getId(),
                            userDto.getEmail(),
                            userDto.getName(),
                            System.currentTimeMillis()
                    );

                    new Thread(() -> {
                        database.userDao().insertUser(userEntity);
                        callback.onSuccess(userDto.toUser());
                    }).start();
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void register(String email, String password, String name, AuthCallback callback) {
        // Способ 1: Firebase Auth (сеть)
        firebaseAuth.register(email, password, name)
                .addOnSuccessListener(userDto -> {
                    // Способ 2: SharedPreferences
                    prefs.saveUserData(userDto.getId(), userDto.getEmail(), name);

                    // Способ 3: Room Database
                    UserEntity userEntity = new UserEntity(
                            userDto.getId(),
                            userDto.getEmail(),
                            name,
                            System.currentTimeMillis()
                    );

                    new Thread(() -> {
                        database.userDao().insertUser(userEntity);
                        callback.onSuccess(new User(userDto.getId(), userDto.getEmail(), name));
                    }).start();
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public User getCurrentUser() {
        // Приоритет 1: Firebase Auth (самый актуальный)
        UserDto firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.toUser();
        }

        // Приоритет 2: SharedPreferences (быстрая проверка)
        if (prefs.isLoggedIn()) {
            String userId = prefs.getUserId();
            String email = prefs.getUserEmail();
            String name = prefs.getUserName();

            if (userId != null && email != null) {
                return new User(userId, email, name != null ? name : "User");
            }
        }

        // Приоритет 3: Room Database (резервное хранилище)
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
        // Проверяем, вошел ли пользователь через любой из источников данных
        return firebaseAuth.getCurrentUser() != null || prefs.isLoggedIn();
    }
    @Override
    public void logout() {
        // Способ 1: Firebase logout
        firebaseAuth.logout();

        // Способ 2: Очищаем SharedPreferences
        prefs.clearUserData();

        // Способ 3: Удаляем из Room Database (опционально)
        String userId = prefs.getUserId();
        if (userId != null) {
            new Thread(() -> {
                database.userDao().deleteUser(userId);
            }).start();
        }
    }

    // Дополнительные методы для демонстрации всех трех способов
    public String getStorageInfo() {
        StringBuilder info = new StringBuilder();

        // Информация из SharedPreferences
        info.append("SharedPreferences: ")
                .append(prefs.isLoggedIn() ? "User logged in" : "No user data")
                .append("\n");

        // Информация из Room
        new Thread(() -> {
            int userCount = database.userDao().getUserCount();
            // Можно передать через callback или LiveData
        }).start();

        // Информация из Firebase
        UserDto firebaseUser = firebaseAuth.getCurrentUser();
        info.append("Firebase Auth: ")
                .append(firebaseUser != null ? "User active" : "No active session");

        return info.toString();
    }
}