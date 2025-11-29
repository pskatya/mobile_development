package ru.mirea.pasportnikovaeo.bookshell.ui.profile;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.domain.model.UserStats;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.domain.repositories.UserStatsRepository;

public class ProfileViewModel extends ViewModel {
    private AuthRepository authRepository;
    private UserStatsRepository userStatsRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<UserStats> userStats = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>(false);

    // Добавляем LiveData для количества лайкнутых книг
    private MutableLiveData<Integer> likedBooksCount = new MutableLiveData<>(0);

    public ProfileViewModel(AuthRepository authRepository, UserStatsRepository userStatsRepository) {
        this.authRepository = authRepository;
        this.userStatsRepository = userStatsRepository;
        loadUserData();
    }

    public void loadUserData() {
        User user = authRepository.getCurrentUser();
        if (user != null) {
            currentUser.setValue(user);
            loadUserStats(user.getId());
        }
    }

    private void loadUserStats(String userId) {
        isLoading.setValue(true);
        userStatsRepository.getUserStats(userId, new UserStatsRepository.UserStatsCallback() {
            @Override
            public void onSuccess(UserStats stats) {
                isLoading.postValue(false);
                userStats.postValue(stats);
                // Обновляем количество лайкнутых книг
                likedBooksCount.postValue(stats.getBooksLiked());
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                error.postValue("Failed to load user stats: " + e.getMessage());
            }
        });
    }

    // Метод для обновления количества лайкнутых книг
    public void updateLikedBooksCount(int count) {
        likedBooksCount.setValue(count);

        // Также обновляем в базе данных
        User user = currentUser.getValue();
        if (user != null) {
            userStatsRepository.updateBooksLiked(user.getId(), count, new UserStatsRepository.UpdateCallback() {
                @Override
                public void onSuccess() {
                    // Успешно обновили в базе
                }

                @Override
                public void onError(Exception e) {
                    error.postValue("Failed to update books liked count: " + e.getMessage());
                }
            });
        }
    }

    public void updateUserName(String newName) {
        User user = currentUser.getValue();
        if (user != null && !newName.trim().isEmpty()) {
            isLoading.setValue(true);
            authRepository.updateUserName(user.getId(), newName.trim(), new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User updatedUser) {
                    // Используем postValue для потокобезопасности
                    isLoading.postValue(false);
                    currentUser.postValue(updatedUser);
                    updateSuccess.postValue(true);

                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                        updateSuccess.postValue(false);
                    }, 2000);
                }

                @Override
                public void onError(Exception e) {
                    isLoading.postValue(false);
                    error.postValue("Failed to update name: " + e.getMessage());
                }
            });
        }
    }

    public void logout() {
        authRepository.logout();
        currentUser.setValue(null);
        userStats.setValue(null);
        likedBooksCount.setValue(0);
    }

    // LiveData геттеры
    public LiveData<User> getCurrentUser() { return currentUser; }
    public LiveData<UserStats> getUserStats() { return userStats; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }
    public LiveData<Integer> getLikedBooksCount() { return likedBooksCount; }
}