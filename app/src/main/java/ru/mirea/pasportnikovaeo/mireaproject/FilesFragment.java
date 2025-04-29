package ru.mirea.pasportnikovaeo.mireaproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesFragment extends Fragment {
    private List<String> filesList = new ArrayList<>();
    private FilesAdapter adapter;
    private static final String FILE_EXTENSION = ".enc";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.filesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FilesAdapter(filesList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddFile);
        fab.setOnClickListener(v -> showCreateFileDialog());

        loadFilesList();

        return view;
    }

    private void showCreateFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Создать зашифрованный файл");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_file, null);
        EditText fileNameEditText = dialogView.findViewById(R.id.fileNameEditText);
        EditText contentEditText = dialogView.findViewById(R.id.contentEditText);

        builder.setView(dialogView);
        builder.setPositiveButton("Создать", (dialog, which) -> {
            String fileName = fileNameEditText.getText().toString();
            String content = contentEditText.getText().toString();
            createEncryptedFile(fileName, content);
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void createEncryptedFile(String fileName, String content) {
        try {
            File file = new File(requireContext().getFilesDir(), fileName + FILE_EXTENSION);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encryptContent(content).getBytes());
            fos.close();

            filesList.add(file.getName());
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Файл создан", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
        }
    }

    private String encryptContent(String content) {
        // Простейшая "шифровка" - инвертирование строки
        return new StringBuilder(content).reverse().toString();
    }

    private void loadFilesList() {
        File[] files = requireContext().getFilesDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(FILE_EXTENSION)) {
                    filesList.add(file.getName());
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}