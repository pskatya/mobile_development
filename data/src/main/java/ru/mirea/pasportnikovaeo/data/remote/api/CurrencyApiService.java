package ru.mirea.pasportnikovaeo.data.remote.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyApiService {

    // Для exchangerate-api.com и open.er-api.com
    @GET("v4/latest/USD")
    Call<CurrencyResponse> getCurrencyRates();

    // Для frankfurter.app
    @GET("latest?from=EUR&to=USD,RUB")
    Call<CurrencyResponse> getAlternativeRates();
}