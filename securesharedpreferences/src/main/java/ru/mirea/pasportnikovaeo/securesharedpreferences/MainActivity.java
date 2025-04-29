package ru.mirea.pasportnikovaeo.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences encryptedSharedPreferences;
    private TextView poetNameTextView;
    private ImageView poetImageView;
    private static final String POET_NAME_KEY = "POET_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poetNameTextView = findViewById(R.id.poetNameTextView);
        poetImageView = findViewById(R.id.poetImageView);
        Button saveButton = findViewById(R.id.saveButton);

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            loadPoetData();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка безопасности: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        saveButton.setOnClickListener(v -> {
            savePoetData();
            loadPoetData();
        });
    }

    private void savePoetData() {
        try {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.putString(POET_NAME_KEY, "Александр Сергеевич Пушкин");
            editor.apply();
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPoetData() {
        try {
            String poetName = encryptedSharedPreferences.getString(POET_NAME_KEY, "Данные не найдены");
            poetNameTextView.setText(poetName);

            Bitmap poetImage = BitmapFactory.decodeResource(getResources(), R.drawable.pushkin);
            poetImageView.setImageBitmap(poetImage);
        } catch (Exception e) {
            e.printStackTrace();
            poetNameTextView.setText("Ошибка загрузки данных");
        }
    }
}