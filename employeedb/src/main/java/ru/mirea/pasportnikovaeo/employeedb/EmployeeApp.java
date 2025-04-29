package ru.mirea.pasportnikovaeo.employeedb;

import android.app.Application;

public class EmployeeApp extends Application {
    private static EmployeeApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);
    }

    public static EmployeeApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
