package ru.mirea.pasportnikovaeo.data.remote.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import ru.mirea.pasportnikovaeo.data.model.UserDto;

public class FirebaseAuthSource {
    private FirebaseAuth auth;

    public FirebaseAuthSource() {
        auth = FirebaseAuth.getInstance();
    }

    public Task<UserDto> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        return new UserDto(
                                user.getUid(),
                                user.getEmail(),
                                user.getDisplayName() != null ? user.getDisplayName() : "User"
                        );
                    } else {
                        throw task.getException();
                    }
                });
    }

    public Task<UserDto> register(String email, String password, String name) {
        return auth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser user = task.getResult().getUser();

                        // ОБНОВЛЯЕМ ПРОФИЛЬ ПОЛЬЗОВАТЕЛЯ С ИМЕНЕМ
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        return user.updateProfile(profileUpdates)
                                .continueWith(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        return new UserDto(user.getUid(), user.getEmail(), name);
                                    } else {
                                        // Если не удалось обновить профиль, все равно возвращаем пользователя
                                        // но с именем из параметра (оно сохранится в SharedPreferences)
                                        return new UserDto(user.getUid(), user.getEmail(), name);
                                    }
                                });
                    } else {
                        throw task.getException();
                    }
                });
    }

    public UserDto getCurrentUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return new UserDto(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName() != null ? user.getDisplayName() : "User"
            );
        }
        return null;
    }

    public void logout() {
        auth.signOut();
    }

    // Дополнительный метод для обновления имени в Firebase
    public Task<Void> updateUserName(String name) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            return user.updateProfile(profileUpdates);
        }
        return Tasks.forException(new Exception("No user logged in"));
    }
}