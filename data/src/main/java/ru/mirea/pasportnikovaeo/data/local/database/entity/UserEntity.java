package ru.mirea.pasportnikovaeo.data.local.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ru.mirea.pasportnikovaeo.data.local.database.converters.DateConverter;

@Entity(tableName = "users")
@TypeConverters(DateConverter.class)
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String email;
    public String name;
    public long lastLogin;

    public UserEntity(@NonNull String id, String email, String name, long lastLogin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastLogin = lastLogin;
    }
}