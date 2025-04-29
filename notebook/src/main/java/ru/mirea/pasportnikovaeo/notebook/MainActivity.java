package ru.mirea.pasportnikovaeo.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText fileNameInput;
    private EditText quoteInput;
    private Button saveButton;
    private Button loadButton;

    private static final int REQUEST_CODE_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileNameInput = findViewById(R.id.fileNameInput);
        quoteInput = findViewById(R.id.quoteInput);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);

        // Проверка и запрос разрешений
        checkPermissions();

        saveButton.setOnClickListener(v -> saveToFile());
        loadButton.setOnClickListener(v -> loadFromFile());

        // Создаем директорию Directory_Documents, если её нет
        createDirectory();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }

    private void createDirectory() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(path, "Directory_Documents");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void saveToFile() {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Внешнее хранилище недоступно для записи", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = fileNameInput.getText().toString().trim();
        String quote = quoteInput.getText().toString().trim();

        if (fileName.isEmpty() || quote.isEmpty()) {
            Toast.makeText(this, "Введите название файла и цитату", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(path, "Directory_Documents");
        File file = new File(directory, fileName + ".txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(quote);
            output.close();
            Toast.makeText(this, "Файл сохранен: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            if (fileName.equals("Цитата1")) {
                File file2 = new File(directory, "Цитата2.txt");
                FileOutputStream fos2 = new FileOutputStream(file2);
                OutputStreamWriter ow2 = new OutputStreamWriter(fos2);
                ow2.write("Еще одна известная цитата");
                ow2.close();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при сохранении: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromFile() {
        if (!isExternalStorageReadable()) {
            Toast.makeText(this, "Внешнее хранилище недоступно для чтения", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = fileNameInput.getText().toString().trim();
        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите название файла", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(path, "Directory_Documents");
        File file = new File(directory, fileName + ".txt");

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
            quoteInput.setText(stringBuilder.toString().trim());
            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при загрузке: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}