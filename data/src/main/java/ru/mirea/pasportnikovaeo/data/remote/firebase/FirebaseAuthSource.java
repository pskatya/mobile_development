package ru.mirea.pasportnikovaeo.data.remote.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
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
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        return new UserDto(user.getUid(), user.getEmail(), name);
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
}