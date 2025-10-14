package ru.mirea.pasportnikovaeo.data.local.database.dao;

import androidx.room.*;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserEntity;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("DELETE FROM users WHERE id = :userId")
    void deleteUser(String userId);

    @Query("SELECT COUNT(*) FROM users WHERE id = :userId")
    int userExists(String userId);

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}