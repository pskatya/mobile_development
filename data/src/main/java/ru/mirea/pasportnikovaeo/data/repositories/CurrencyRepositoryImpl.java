package ru.mirea.pasportnikovaeo.data.repositories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.pasportnikovaeo.data.remote.api.CurrencyApiService;
import ru.mirea.pasportnikovaeo.data.remote.api.CurrencyResponse;
import ru.mirea.pasportnikovaeo.domain.repositories.CurrencyRepository;
import ru.mirea.pasportnikovaeo.domain.model.CurrencyRates;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    private static final String[] API_BASE_URLS = {
            "https://api.exchangerate-api.com/",
            "https://api.frankfurter.app/",
            "https://open.er-api.com/"
    };

    @Override
    public void getCurrencyRates(CurrencyCallback callback) {
        tryNextApi(0, callback);
    }

    private void tryNextApi(int apiIndex, CurrencyCallback callback) {
        if (apiIndex >= API_BASE_URLS.length) {
            // Все API упали - используем заглушку
            useFallbackRates(callback);
            return;
        }

        String baseUrl = API_BASE_URLS[apiIndex];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyApiService api = retrofit.create(CurrencyApiService.class);

        // Определяем endpoint в зависимости от API
        Call<CurrencyResponse> call;
        if (baseUrl.contains("frankfurter")) {
            call = api.getAlternativeRates();
        } else {
            call = api.getCurrencyRates();
        }

        call.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyRates rates = convertApiResponse(response.body(), baseUrl);
                    callback.onSuccess(rates);
                } else {
                    // Пробуем следующий API
                    tryNextApi(apiIndex + 1, callback);
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                // Пробуем следующий API
                tryNextApi(apiIndex + 1, callback);
            }
        });
    }

    private void useFallbackRates(CurrencyCallback callback) {
        // ЗАГЛУШКА: фиксированные курсы
        double usdToRub = 95.50;
        double eurToRub = 102.30;

        CurrencyRates fallbackRates = new CurrencyRates(usdToRub, eurToRub);
        callback.onSuccess(fallbackRates);
    }

    private CurrencyRates convertApiResponse(CurrencyResponse response, String baseUrl) {
        if (baseUrl.contains("exchangerate-api")) {
            // exchangerate-api.com: базовая валюта USD
            double usdToRub = response.getRates().getRUB();
            double eurToUsd = response.getRates().getEUR();
            double eurToRub = usdToRub * eurToUsd;
            return new CurrencyRates(usdToRub, eurToRub);

        } else if (baseUrl.contains("frankfurter")) {
            // frankfurter.app: базовая валюта EUR
            double eurToUsd = response.getRates().getUSD();
            double eurToRub = response.getRates().getRUB();
            double usdToRub = eurToRub / eurToUsd;
            return new CurrencyRates(usdToRub, eurToRub);

        } else {
            // open.er-api.com или другие
            double usdToRub = response.getRates().getRUB();
            double eurToRub = response.getRates().getEUR();
            return new CurrencyRates(usdToRub, eurToRub);
        }
    }
}