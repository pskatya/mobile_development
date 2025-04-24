package ru.mirea.pasportnikovaeo.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class BackgroundTaskFragment extends Fragment {
    private TextView tvStatus;
    private Button btnStartTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_task, container, false);
        tvStatus = view.findViewById(R.id.tv_status);
        btnStartTask = view.findViewById(R.id.btn_start_task);

        btnStartTask.setOnClickListener(v -> startBackgroundTask());
        return view;
    }

    private void startBackgroundTask() {
        // Условия для запуска (только Wi-Fi)
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();

        // Создание задачи
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .setConstraints(constraints)
                .build();

        // Запуск задачи
        WorkManager.getInstance(requireContext()).enqueue(workRequest);

        // Отслеживание статуса
        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null) {
                        String status = "Статус: ";
                        switch (workInfo.getState()) {
                            case ENQUEUED: status += "В очереди"; break;
                            case RUNNING: status += "Выполняется..."; break;
                            case SUCCEEDED: status += "Готово!"; break;
                            default: status += "Ошибка: " + workInfo.getState();
                        }
                        tvStatus.setText(status);
                    }
                });
    }
}