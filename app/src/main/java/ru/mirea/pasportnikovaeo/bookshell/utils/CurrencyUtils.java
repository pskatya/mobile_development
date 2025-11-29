package ru.mirea.pasportnikovaeo.bookshell.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrencyUtils {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_USD_TO_RUB = "usd_to_rub";
    private static final String KEY_EUR_TO_RUB = "eur_to_rub";
    private static final float DEFAULT_USD = 95.50f;
    private static final float DEFAULT_EUR = 102.30f;

    public static float getUsdToRub(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_USD_TO_RUB, DEFAULT_USD);
    }

    public static float getEurToRub(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_EUR_TO_RUB, DEFAULT_EUR);
    }

    public static String getFormattedRates(Context context) {
        float usd = getUsdToRub(context);
        float eur = getEurToRub(context);
        return String.format("USD: %.2f RUB", usd);
    }

    public static double convertUsdToRub(Context context, double usdPrice) {
        return usdPrice * getUsdToRub(context);
    }

    public static String formatPriceInRub(Context context, double usdPrice) {
        double rubPrice = convertUsdToRub(context, usdPrice);
        return String.format("%.2f RUB", rubPrice);
    }
}