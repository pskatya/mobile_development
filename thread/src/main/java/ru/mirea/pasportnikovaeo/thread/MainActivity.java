package ru.mirea.pasportnikovaeo.thread;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import ru.mirea.pasportnikovaeo.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;
    private static final String TAG = "ThreadProject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Отображение информации о главном потоке
        Thread mainThread = Thread.currentThread();
        binding.infoTextView.setText("Имя текущего потока: " + mainThread.getName());

        mainThread.setName("WORK THREAD PRIORITY: " + mainThread.getPriority() +
                ", THREAD GROUP: " + (mainThread.getThreadGroup() != null ?
                mainThread.getThreadGroup().getName() : "null"));
        binding.infoTextView.append("\nНовое имя потока: " + mainThread.getName());

        // Вывод стэка вызовов
        StringBuilder stackTrace = new StringBuilder("\nStack:");
        for (StackTraceElement element : mainThread.getStackTrace()) {
            stackTrace.append("\n").append(element.toString());
        }
        binding.infoTextView.append(stackTrace.toString());

        // Обработчик кнопки (ваш фрагмент)
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d(TAG, String.format("Запущен поток № %d студентом группы № %s номер по списку № %d",
                                numberThread, "БСБО-09-22", 22));

                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d(TAG, "Выполнен поток № " + numberThread);
                        }
                    }
                }).start();
            }
        });

        // Обработчик кнопки для расчета среднего
        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAveragePairs();
            }
        });
    }

    private void calculateAveragePairs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Имитация долгих вычислений (20 секунд)
                long endTime = System.currentTimeMillis() + 20 * 1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                // Реальные вычисления
                int totalPairs = 100;
                int studyDays = 20;
                double average = (double) totalPairs / studyDays;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.infoTextView.setText(
                                String.format("Среднее количество пар в день: %.2f", average)
                        );
                    }
                });
            }
        }).start();
    }
}