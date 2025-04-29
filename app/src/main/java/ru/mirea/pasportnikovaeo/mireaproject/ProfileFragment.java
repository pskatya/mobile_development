package ru.mirea.pasportnikovaeo.mireaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private static final String PREFS_NAME = "ProfilePrefs";
    private EditText nameEditText, ageEditText, emailEditText;
    private RadioGroup genderRadioGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);

        Button saveButton = view.findViewById(R.id.saveButton);
        Button loadButton = view.findViewById(R.id.loadButton);
        Button exportButton = view.findViewById(R.id.exportButton);

        saveButton.setOnClickListener(v -> saveProfileData());
        loadButton.setOnClickListener(v -> loadProfileData());
        exportButton.setOnClickListener(v -> exportProfileData());

        return view;
    }

    private void saveProfileData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        try {
            editor.putString("name", nameEditText.getText().toString());
            editor.putInt("age", Integer.parseInt(ageEditText.getText().toString()));
            editor.putString("email", emailEditText.getText().toString());
            editor.putInt("gender", genderRadioGroup.getCheckedRadioButtonId());

            editor.apply();
            Toast.makeText(getContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ошибка: проверьте введенные данные", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        nameEditText.setText(sharedPref.getString("name", ""));
        ageEditText.setText(String.valueOf(sharedPref.getInt("age", 0)));
        emailEditText.setText(sharedPref.getString("email", ""));

        int genderId = sharedPref.getInt("gender", -1);
        if (genderId != -1) {
            genderRadioGroup.check(genderId);
        }

        Toast.makeText(getContext(), "Профиль загружен", Toast.LENGTH_SHORT).show();
    }

    private void exportProfileData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String gender = sharedPref.getInt("gender", -1) == R.id.maleRadioButton ? "Мужской" : "Женский";

        String profileData = "=== Мой профиль ===\n" +
                "Имя: " + sharedPref.getString("name", "") + "\n" +
                "Возраст: " + sharedPref.getInt("age", 0) + "\n" +
                "Email: " + sharedPref.getString("email", "") + "\n" +
                "Пол: " + gender + "\n" +
                "==================";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, profileData);

        startActivity(Intent.createChooser(sendIntent, "Экспорт профиля"));
    }
}