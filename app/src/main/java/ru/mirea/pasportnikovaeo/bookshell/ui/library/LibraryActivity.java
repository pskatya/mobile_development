package ru.mirea.pasportnikovaeo.bookshell.ui.library;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityLibraryBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.main.MainActivity;

public class LibraryActivity extends AppCompatActivity {
    private ActivityLibraryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Показываем фрагмент библиотеки
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LibraryFragment())
                    .commit();
        }

        setupBottomNavigation();
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_library);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_library);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Возвращаемся на главную
                Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_library) {
                // Уже в библиотеке
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Toast.makeText(this, "Profile screen coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Подсвечиваем текущий пункт меню
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_library);
    }
}