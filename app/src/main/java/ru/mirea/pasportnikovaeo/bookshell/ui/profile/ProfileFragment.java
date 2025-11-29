package ru.mirea.pasportnikovaeo.bookshell.ui.profile;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;

import ru.mirea.pasportnikovaeo.bookshell.databinding.FragmentProfileBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.auth.AuthActivity;
import ru.mirea.pasportnikovaeo.domain.model.User;
import ru.mirea.pasportnikovaeo.domain.model.UserStats;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;
import ru.mirea.pasportnikovaeo.domain.repositories.UserStatsRepository;
import ru.mirea.pasportnikovaeo.data.repositories.AuthRepositoryImpl;
import ru.mirea.pasportnikovaeo.data.repositories.UserStatsRepositoryImpl;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private AuthRepository authRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRepositories();
        initViewModel();
        setupUI();
        setupObservers();

        // Загружаем данные пользователя
        profileViewModel.loadUserData();
    }

    private void initRepositories() {
        authRepository = new AuthRepositoryImpl(requireContext());
    }

    private boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }

    private void initViewModel() {
        AuthRepository authRepository = new AuthRepositoryImpl(requireContext());
        UserStatsRepository statsRepository = new UserStatsRepositoryImpl(requireContext());

        ProfileViewModelFactory factory = new ProfileViewModelFactory(authRepository, statsRepository);
        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
    }

    private void setupUI() {
        // Кнопка выхода (только для авторизованных)
        binding.buttonLogout.setOnClickListener(v -> logoutUser());

        // Кнопка обновления профиля (только для авторизованных)
        binding.buttonUpdateProfile.setOnClickListener(v -> updateUserProfile());

        // Кнопка настроек
        binding.buttonSettings.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Settings screen coming soon", Toast.LENGTH_SHORT).show();
        });

        // Кнопка входа (для неавторизованных)
        binding.buttonLogin.setOnClickListener(v -> {
            navigateToAuth();
        });
    }

    private void navigateToAuth() {
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setupObservers() {
        // Наблюдаем за данными пользователя
        profileViewModel.getCurrentUser().observe(getViewLifecycleOwner(), this::displayUserData);

        // Наблюдаем за статистикой пользователя
        profileViewModel.getUserStats().observe(getViewLifecycleOwner(), this::displayUserStats);

        // Наблюдаем за состоянием загрузки
        profileViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                binding.buttonLogout.setText("Logging out...");
                binding.buttonLogout.setEnabled(false);
                binding.buttonUpdateProfile.setText("Updating...");
                binding.buttonUpdateProfile.setEnabled(false);
            } else {
                binding.buttonLogout.setText("Logout");
                binding.buttonLogout.setEnabled(true);
                binding.buttonUpdateProfile.setText("Update Profile");
                binding.buttonUpdateProfile.setEnabled(true);
            }
        });

        // Наблюдаем за ошибками
        profileViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Наблюдаем за успешным обновлением
        profileViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData(User user) {
        if (user != null && isUserLoggedIn()) {
            binding.textUserName.setText(user.getName());
            binding.textUserEmail.setText(user.getEmail());
            binding.textUserId.setText("ID: " + user.getId());

            // Показываем основную информацию для авторизованных
            binding.userInfoLayout.setVisibility(View.VISIBLE);
            binding.loginPrompt.setVisibility(View.GONE);
            binding.profileActions.setVisibility(View.VISIBLE);
        } else {
            showLoginScreen();
        }
    }

    private void displayUserStats(UserStats stats) {
        if (stats != null && isUserLoggedIn()) {
            binding.textBooksRead.setText("Books read: " + stats.getBooksRead());
            binding.textBooksLiked.setText("Books liked: " + stats.getBooksLiked());

            // Форматируем дату регистрации
            String memberSince = formatDate(stats.getMemberSince());
            binding.textMemberSince.setText("Member since: " + memberSince);

            binding.statsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.statsLayout.setVisibility(View.GONE);
        }
    }

    private String formatDate(long timestamp) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault());
            return sdf.format(new java.util.Date(timestamp));
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private void showLoginScreen() {
        binding.userInfoLayout.setVisibility(View.GONE);
        binding.statsLayout.setVisibility(View.GONE);
        binding.profileActions.setVisibility(View.GONE);
        binding.loginPrompt.setVisibility(View.VISIBLE);
    }

    private void logoutUser() {
        if (!isUserLoggedIn()) {
            showLoginScreen();
            return;
        }

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    profileViewModel.logout();
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    showLoginScreen();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateUserProfile() {
        if (!isUserLoggedIn()) {
            Toast.makeText(requireContext(), "Please login to update profile", Toast.LENGTH_SHORT).show();
            return;
        }

        String newName = binding.editUserName.getText().toString().trim();
        if (!newName.isEmpty()) {
            profileViewModel.updateUserName(newName);
            binding.editUserName.setText(""); // Очищаем поле после обновления
        } else {
            Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Обновляем данные при возвращении на экран
        profileViewModel.loadUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}