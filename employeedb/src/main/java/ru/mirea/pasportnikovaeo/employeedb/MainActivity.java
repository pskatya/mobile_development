package ru.mirea.pasportnikovaeo.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "EmployeeDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = EmployeeApp.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        // Создание супер-героев
        Employee hero1 = new Employee("Superman", 50000, "Flight, Super strength");
        Employee hero2 = new Employee("Batman", 1000000, "Intelligence, Martial arts");
        Employee hero3 = new Employee("Spider-Man", 30000, "Wall-crawling, Spider-sense");

        // Добавление в базу данных
        employeeDao.insert(hero1);
        employeeDao.insert(hero2);
        employeeDao.insert(hero3);

        // Получение всех сотрудников
        List<Employee> employees = employeeDao.getAll();
        for (Employee emp : employees) {
            Log.d(TAG, "Hero: " + emp.name + ", Salary: " + emp.salary + ", Power: " + emp.superpower);
        }

        // Обновление записи
        Employee batman = employeeDao.getById(2);
        if (batman != null) {
            batman.salary = 1500000;
            employeeDao.update(batman);
            Log.d(TAG, "Updated Batman's salary to: " + batman.salary);
        }

        // Поиск по суперспособности
        List<Employee> flyingHeroes = employeeDao.getBySuperpower("%Flight%");
        for (Employee hero : flyingHeroes) {
            Log.d(TAG, "Flying hero: " + hero.name);
        }
    }
}