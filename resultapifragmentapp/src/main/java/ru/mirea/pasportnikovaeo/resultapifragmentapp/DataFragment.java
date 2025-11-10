package ru.mirea.pasportnikovaeo.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        EditText editText = view.findViewById(R.id.editTextInfo);
        Button button = view.findViewById(R.id.buttonSend);

        button.setOnClickListener(v -> {
            String text = editText.getText().toString();

            // Передача данных через Fragment Result API
            Bundle result = new Bundle();
            result.putString("data_key", text);
            getParentFragmentManager().setFragmentResult("request_key", result);

            // Показать BottomSheet
            new ResultBottomSheet().show(getParentFragmentManager(), "ResultBottomSheet");
        });

        return view;
    }
}