package ru.mirea.pasportnikovaeo.data.remote.api;

import com.google.gson.annotations.SerializedName;

public class CurrencyResponse {

    @SerializedName("rates")
    private Rates rates;

    @SerializedName("conversion_rates")
    private Rates conversionRates;

    public Rates getRates() {
        if (rates != null) {
            return rates;
        }
        if (conversionRates != null) {
            return conversionRates;
        }
        return new Rates();
    }

    public static class Rates {
        @SerializedName("RUB")
        private double RUB = 0.0;

        @SerializedName("EUR")
        private double EUR = 0.0;

        @SerializedName("USD")
        private double USD = 0.0;

        public double getRUB() {
            return RUB != 0.0 ? RUB : 95.50; // Fallback если API вернул 0
        }

        public double getEUR() {
            return EUR != 0.0 ? EUR : 0.92; // Fallback если API вернул 0
        }

        public double getUSD() {
            return USD != 0.0 ? USD : 1.0; // Fallback если API вернул 0
        }
    }
}