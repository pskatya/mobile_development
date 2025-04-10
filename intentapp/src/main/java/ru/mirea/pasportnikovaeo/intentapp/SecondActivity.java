package ru.mirea.pasportnikovaeo.intentapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = findViewById(R.id.textView);

        // Получение данных из Intent
        String time = getIntent().getStringExtra("TIME");
        int squaredNum = getIntent().getIntExtra("SQUARED_NUM", 0);

        // Формирование строки для отображения
        String displayText = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ "
                + squaredNum + ", а текущее время " + time;

        textView.setText(displayText);
    }
}