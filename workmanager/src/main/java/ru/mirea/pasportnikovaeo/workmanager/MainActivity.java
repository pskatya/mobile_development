package ru.mirea.pasportnikovaeo.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Условия для запуска задачи
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(false)
                .build();

        // Создание запроса с ограничениями
        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)
                .build();

        // Запуск задачи
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null) {
                        Log.d("WorkManagerStatus", "State: " + workInfo.getState());
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Log.d("WorkManagerStatus", "Work completed successfully!");
                        } else if (workInfo.getState() == WorkInfo.State.BLOCKED) {
                            Log.d("WorkManagerStatus", "Work blocked (constraints not met)");
                        }
                    }
                });
    }

}