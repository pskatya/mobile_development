package ru.mirea.pasportnikovaeo.scrollviewapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private LinearLayout wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrapper = findViewById(R.id.wrapper);
        displayGeometricProgression();
    }

    private void displayGeometricProgression() {
        BigDecimal firstTerm = BigDecimal.ONE;
        BigDecimal denominator = new BigDecimal("2");

        for (int i = 1; i <= 100; i++) {
            BigDecimal term = firstTerm.multiply(denominator.pow(i - 1));

            View itemView = LayoutInflater.from(this).inflate(R.layout.item, wrapper, false);

            TextView textView = itemView.findViewById(R.id.textView);
            String itemText = i + ". " + term.toString();
            textView.setText(itemText);

            // Добавляем элемент в контейнер
            wrapper.addView(itemView);
        }
    }
}