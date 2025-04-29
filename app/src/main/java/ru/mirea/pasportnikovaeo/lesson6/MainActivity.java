package ru.mirea.pasportnikovaeo.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.pasportnikovaeo.lesson6.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        private EditText groupEditText, numberEditText, movieEditText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            groupEditText = findViewById(R.id.groupEditText);
            numberEditText = findViewById(R.id.numberEditText);
            movieEditText = findViewById(R.id.movieEditText);

            Button saveButton = findViewById(R.id.saveButton);
            saveButton.setOnClickListener(v -> savePreferences());

            loadPreferences();
        }

        private void savePreferences() {
            SharedPreferences sharedPref = getSharedPreferences("student_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("GROUP", groupEditText.getText().toString());
            editor.putInt("NUMBER", Integer.parseInt(numberEditText.getText().toString()));
            editor.putString("MOVIE", movieEditText.getText().toString());

            editor.apply();
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        }

        private void loadPreferences() {
            SharedPreferences sharedPref = getSharedPreferences("student_prefs", MODE_PRIVATE);

            groupEditText.setText(sharedPref.getString("GROUP", ""));
            numberEditText.setText(String.valueOf(sharedPref.getInt("NUMBER", 0)));
            movieEditText.setText(sharedPref.getString("MOVIE", ""));
        }
    }