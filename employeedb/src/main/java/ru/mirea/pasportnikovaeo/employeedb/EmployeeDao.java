package ru.mirea.pasportnikovaeo.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employees")
    List<Employee> getAll();

    @Query("SELECT * FROM employees WHERE id = :id")
    Employee getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    // Дополнительные запросы для супер-героев
    @Query("SELECT * FROM employees WHERE superpower LIKE :power")
    List<Employee> getBySuperpower(String power);

    @Query("SELECT * FROM employees WHERE salary > :minSalary")
    List<Employee> getByMinSalary(int minSalary);
}
