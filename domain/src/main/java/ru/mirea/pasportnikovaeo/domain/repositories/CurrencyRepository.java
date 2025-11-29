package ru.mirea.pasportnikovaeo.domain.repositories;

import ru.mirea.pasportnikovaeo.domain.model.CurrencyRates;

public interface CurrencyRepository {
    interface CurrencyCallback {
        void onSuccess(CurrencyRates rates);
        void onError(Exception e);
    }

    void getCurrencyRates(CurrencyCallback callback);
}