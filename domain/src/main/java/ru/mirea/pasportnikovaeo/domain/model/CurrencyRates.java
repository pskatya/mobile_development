package ru.mirea.pasportnikovaeo.domain.model;

public class CurrencyRates {
    private double usdToRub;
    private double eurToRub;

    public CurrencyRates(double usdToRub, double eurToRub) {
        this.usdToRub = usdToRub;
        this.eurToRub = eurToRub;
    }

    public double getUsdToRub() { return usdToRub; }
    public double getEurToRub() { return eurToRub; }
}