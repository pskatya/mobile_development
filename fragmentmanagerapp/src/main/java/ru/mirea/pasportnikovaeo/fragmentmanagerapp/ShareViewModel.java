package ru.mirea.pasportnikovaeo.fragmentmanagerapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<String> selectedCountry = new MutableLiveData<>();

    public void selectCountry(String country) {
        selectedCountry.setValue(country);
    }

    public MutableLiveData<String> getSelectedCountry() {
        return selectedCountry;
    }
}
