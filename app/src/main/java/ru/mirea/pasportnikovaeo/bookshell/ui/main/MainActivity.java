package ru.mirea.pasportnikovaeo.bookshell.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityMainBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.auth.AuthActivity;
import ru.mirea.pasportnikovaeo.bookshell.ui.library.LibraryFragment;
import ru.mirea.pasportnikovaeo.bookshell.ui.profile.ProfileFragment;
import ru.mirea.pasportnikovaeo.bookshell.utils.CurrencyUtils;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.repositories.AuthRepositoryImpl;
import ru.mirea.pasportnikovaeo.data.repositories.BookRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding; // Сделать public
    private BookViewModel bookViewModel;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRepositories();
        checkAuthentication();
        initViewModel();
        setupBottomNavigation();
        updateCurrencyBadge();

        // Показываем HomeFragment по умолчанию
        if (savedInstanceState == null) {
            showHomeFragment();
        }
    }

    private void initRepositories() {
        authRepository = new AuthRepositoryImpl(this);
    }

    private void checkAuthentication() {
        if (!authRepository.isUserLoggedIn()) {
            // Если пользователь не авторизован, переходим на экран авторизации
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViewModel() {
        AppDatabase database = AppDatabase.getInstance(this);
        BookRepositoryImpl bookRepository = new BookRepositoryImpl(database);
        BookViewModelFactory factory = new BookViewModelFactory(bookRepository);
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                showHomeFragment();
                return true;
            } else if (itemId == R.id.navigation_library) {
                showLibraryFragment();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                showProfileFragment();
                return true;
            }
            return false;
        });
    }

    // Сделать методы public
    public void showHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    public void showLibraryFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LibraryFragment())
                .commit();
    }

    public void showProfileFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();
    }

    private void updateCurrencyBadge() {
        String currentRates = CurrencyUtils.getFormattedRates(this);
        binding.currencyBadge.setText(currentRates);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeFragment) {
            super.onBackPressed();
        } else {
            showHomeFragment();
            binding.bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}