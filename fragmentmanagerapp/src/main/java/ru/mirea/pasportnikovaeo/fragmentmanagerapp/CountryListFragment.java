package ru.mirea.pasportnikovaeo.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CountryListFragment extends Fragment {

    private ShareViewModel viewModel;
    private final String[] countries = {"Россия", "США", "Германия", "Франция", "Япония"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        ListView listView = view.findViewById(R.id.listViewCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                countries
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCountry = countries[position];
            viewModel.selectCountry(selectedCountry);

            // Переход к деталям
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CountryDetailsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}

