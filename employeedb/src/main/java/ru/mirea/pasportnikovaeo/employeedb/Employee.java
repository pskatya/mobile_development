package ru.mirea.pasportnikovaeo.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employees")
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int salary;
    public String superpower; // Добавим поле для суперспособности

    public Employee() {}

    public Employee(String name, int salary, String superpower) {
        this.name = name;
        this.salary = salary;
        this.superpower = superpower;
    }
}
