package ru.mirea.pasportnikovaeo.data_thread;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);

        // Создаем Runnable объекты
        final Runnable runn1 = new Runnable() {
            public void run() {
                tvInfo.append("\nrunn1 выполнен в UI потоке");
            }
        };

        final Runnable runn2 = new Runnable() {
            public void run() {
                tvInfo.append("\nrunn2 выполнен, post - добавление в очередь UI потока");
            }
        };

        final Runnable runn3 = new Runnable() {
            public void run() {
                tvInfo.append("\nrunn3 выполнен, postDelayed - добавление в очередь UI потока с задержкой");
            }
        };

        // Обработчик кнопки
        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInfo.setText("Начало выполнения...");

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            runOnUiThread(runn1);

                            TimeUnit.SECONDS.sleep(1);
                            tvInfo.postDelayed(runn3, 2000);
                            tvInfo.post(runn2);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}