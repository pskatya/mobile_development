package ru.mirea.pasportnikovaeo.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ResultBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_result, container, false);

        TextView textView = view.findViewById(R.id.textViewResult);

        getParentFragmentManager().setFragmentResultListener("request_key", this, (requestKey, bundle) -> {
            String result = bundle.getString("data_key");
            textView.setText("Получено: " + result);
        });

        return view;
    }
}
