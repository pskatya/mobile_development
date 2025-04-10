package ru.mirea.pasportnikovaeo.intentapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long dateInMillis = System.currentTimeMillis();
        String format = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String dateString = sdf.format(new Date(dateInMillis));

        int studentNumber = 21;
        int squaredNumber = studentNumber * studentNumber;

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("TIME", dateString);
        intent.putExtra("SQUARED_NUM", squaredNumber);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> startActivity(intent));
    }
}