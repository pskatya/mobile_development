package ru.mirea.pasportnikovaeo.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TodoFragment extends Fragment {

    private String[] tasks = {
            "Сделать практическую работу - Выполнено",
            "Подготовиться к экзамену - В процессе",
            "Прочитать лекцию - Не начато",
            "Написать конспект - Выполнено",
            "Выполнить домашнее задание - В процессе"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        TextView studentText = view.findViewById(R.id.textStudentInfo);
        studentText.setText("Список дел студента №21"); // ЗАМЕНИТЕ НА ВАШ НОМЕР

        ListView listView = view.findViewById(R.id.listViewTasks);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                tasks
        );
        listView.setAdapter(adapter);

        return view;
    }
}