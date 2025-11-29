package ru.mirea.pasportnikovaeo.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ru.mirea.pasportnikovaeo.data.local.database.entity.UserStatsEntity;

@Dao
public interface UserStatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserStats(UserStatsEntity userStats);

    @Query("SELECT * FROM user_stats WHERE userId = :userId")
    UserStatsEntity getUserStats(String userId);

    @Query("UPDATE user_stats SET booksRead = :booksRead, lastUpdated = :lastUpdated WHERE userId = :userId")
    void updateBooksRead(String userId, int booksRead, long lastUpdated);

    @Query("UPDATE user_stats SET booksLiked = :booksLiked, lastUpdated = :lastUpdated WHERE userId = :userId")
    void updateBooksLiked(String userId, int booksLiked, long lastUpdated);

    @Query("DELETE FROM user_stats WHERE userId = :userId")
    void deleteUserStats(String userId);
}