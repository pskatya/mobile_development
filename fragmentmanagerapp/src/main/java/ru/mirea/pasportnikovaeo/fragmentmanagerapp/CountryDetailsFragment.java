package ru.mirea.pasportnikovaeo.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CountryDetailsFragment extends Fragment {

    private ShareViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_details, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        TextView textView = view.findViewById(R.id.textViewDetails);

        viewModel.getSelectedCountry().observe(getViewLifecycleOwner(), country -> {
            String details = getCountryDetails(country);
            textView.setText(details);
        });

        return view;
    }

    private String getCountryDetails(String country) {
        switch (country) {
            case "Россия": return "Столица: Москва\nНаселение: 146 млн.";
            case "США": return "Столица: Вашингтон\nНаселение: 331 млн.";
            case "Германия": return "Столица: Берлин\nНаселение: 83 млн.";
            case "Франция": return "Столица: Париж\nНаселение: 67 млн.";
            case "Япония": return "Столица: Токио\nНаселение: 126 млн.";
            default: return "Информация отсутствует";
        }
    }
}
