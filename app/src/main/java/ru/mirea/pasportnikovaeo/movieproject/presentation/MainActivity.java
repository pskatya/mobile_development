package ru.mirea.pasportnikovaeo.movieproject.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.movieproject.R;
import ru.mirea.pasportnikovaeo.movieproject.presentation.viewmodel.MainViewModel;
import ru.mirea.pasportnikovaeo.movieproject.presentation.viewmodel.ViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private EditText editTextMovie;
    private TextView textViewMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewModelFactory factory = new ViewModelFactory(this);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        editTextMovie = findViewById(R.id.editTextMovie);
        textViewMovie = findViewById(R.id.textViewMovie);

        setupObservers();
        setupClickListeners();
    }

    private void setupObservers() {
        // Наблюдаем за результатами операций
        mainViewModel.getMovieResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                textViewMovie.setText(result);
            }
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieName = editTextMovie.getText().toString();
                mainViewModel.saveMovie(movieName);
            }
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.getMovie();
            }
        });
    }
}