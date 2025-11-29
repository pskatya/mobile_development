package ru.mirea.pasportnikovaeo.data.local.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_stats")
public class UserStatsEntity {
    @PrimaryKey
    @NonNull
    public String userId;
    public int booksRead;
    public int booksLiked;
    public long memberSince;
    public long lastUpdated;

    public UserStatsEntity(@NonNull String userId, int booksRead, int booksLiked, long memberSince, long lastUpdated) {
        this.userId = userId;
        this.booksRead = booksRead;
        this.booksLiked = booksLiked;
        this.memberSince = memberSince;
        this.lastUpdated = lastUpdated;
    }
}