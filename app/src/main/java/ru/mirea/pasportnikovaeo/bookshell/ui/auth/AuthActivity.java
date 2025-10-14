package ru.mirea.pasportnikovaeo.bookshell.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityAuthBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.main.MainActivity;
import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.data.repositories.AuthRepositoryImpl;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private boolean isLoginMode = true;

    private AuthRepository authRepository;
    private static final String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "AuthActivity created");

        // Инициализация репозитория
        authRepository = new AuthRepositoryImpl(this);

        // Проверяем, не авторизован ли уже пользователь
        checkCurrentUser();

        setupUI();
    }

    private void checkCurrentUser() {
        if (authRepository.isUserLoggedIn()) {
            User currentUser = authRepository.getCurrentUser();
            Log.d(TAG, "User already logged in: " + currentUser.getEmail());
            navigateToMain();
        } else {
            Log.d(TAG, "No user logged in");
        }
    }

    private void setupUI() {
        // Tab switching
        binding.tabLogin.setOnClickListener(v -> switchToLogin());
        binding.tabRegister.setOnClickListener(v -> switchToRegister());

        // Button click listeners
        binding.btnSignIn.setOnClickListener(v -> performLogin());
        binding.btnCreateAccount.setOnClickListener(v -> performRegister());

        binding.forgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void switchToLogin() {
        isLoginMode = true;
        updateTabAppearance();
        binding.loginForm.setVisibility(View.VISIBLE);
        binding.registerForm.setVisibility(View.GONE);
    }

    private void switchToRegister() {
        isLoginMode = false;
        updateTabAppearance();
        binding.loginForm.setVisibility(View.GONE);
        binding.registerForm.setVisibility(View.VISIBLE);
    }

    private void updateTabAppearance() {
        if (isLoginMode) {
            binding.tabLogin.setBackgroundColor(getColor(R.color.md_theme_light_primary));
            binding.tabLogin.setTextColor(getColor(R.color.md_theme_light_onPrimary));
            binding.tabRegister.setBackgroundColor(getColor(android.R.color.transparent));
            binding.tabRegister.setTextColor(getColor(R.color.md_theme_light_onSurfaceVariant));
        } else {
            binding.tabRegister.setBackgroundColor(getColor(R.color.md_theme_light_primary));
            binding.tabRegister.setTextColor(getColor(R.color.md_theme_light_onPrimary));
            binding.tabLogin.setBackgroundColor(getColor(android.R.color.transparent));
            binding.tabLogin.setTextColor(getColor(R.color.md_theme_light_onSurfaceVariant));
        }
    }

    private void performLogin() {
        String email = binding.loginEmail.getText().toString().trim();
        String password = binding.loginPassword.getText().toString().trim();

        Log.d(TAG, "Login attempt - Email: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        showLoading(true);

        // Используем AuthRepository вместо прямого вызова FirebaseAuth
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.d(TAG, "Login successful: " + user.getEmail());
                    showSuccess("Login successful!");
                    navigateToMain();
                });
            }

            @Override
            public void onError(Exception exception) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.w(TAG, "Login failed: " + exception.getMessage());
                    showError("Login failed: " + exception.getMessage());
                });
            }
        });
    }

    private void performRegister() {
        String email = binding.registerEmail.getText().toString().trim();
        String password = binding.registerPassword.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        String name = binding.registerName.getText().toString().trim();

        Log.d(TAG, "Registration attempt - Email: " + email);

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords don't match");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        showLoading(true);

        // Используем AuthRepository вместо прямого вызова FirebaseAuth
        authRepository.register(email, password, name, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.d(TAG, "Registration successful: " + user.getEmail());
                    showSuccess("Registration successful! Please sign in.");
                    // Переключаем на логин и очищаем поля
                    switchToLogin();
                    clearRegisterFields();
                });
            }

            @Override
            public void onError(Exception exception) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.w(TAG, "Registration failed: " + exception.getMessage());
                    showError("Registration failed: " + exception.getMessage());
                });
            }
        });
    }

    private void clearRegisterFields() {
        binding.registerEmail.setText("");
        binding.registerPassword.setText("");
        binding.confirmPassword.setText("");
        binding.registerName.setText("");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.btnSignIn.setEnabled(false);
            binding.btnCreateAccount.setEnabled(false);
            binding.btnSignIn.setText("Signing In...");
            binding.btnCreateAccount.setText("Creating Account...");
        } else {
            binding.btnSignIn.setEnabled(true);
            binding.btnCreateAccount.setEnabled(true);
            binding.btnSignIn.setText("Sign In");
            binding.btnCreateAccount.setText("Create Account");
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Error: " + message);
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Success: " + message);
    }

    private void showForgotPasswordDialog() {
        Toast.makeText(this, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }
}