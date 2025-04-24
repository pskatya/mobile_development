package ru.mirea.pasportnikovaeo.lesson4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

// Импорт Binding-класса (генерируется автоматически)
import ru.mirea.pasportnikovaeo.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;  // Объявляем Binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Устанавливаем корневое View

        // EdgeToEdge (ваш оригинальный код)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Работа с элементами через Binding
        binding.textViewMirea.setText("MIREA is the best!");  // Меняем текст TextView
        binding.editTextMirea.setText("Мой номер по списку ");    // Добавляем подсказку в EditText

        // Обработчик клика по кнопке
        binding.buttonMirea.setOnClickListener(v -> {
            String n = binding.editTextMirea.getText().toString();
            binding.textViewMirea.setText(n + "!");
        });
    }
}