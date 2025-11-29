package ru.mirea.pasportnikovaeo.data.local.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import ru.mirea.pasportnikovaeo.data.local.database.dao.BookDao;
import ru.mirea.pasportnikovaeo.data.local.database.dao.UserDao;
import ru.mirea.pasportnikovaeo.data.local.database.dao.UserStatsDao;
import ru.mirea.pasportnikovaeo.data.local.database.entity.BookEntity;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserEntity;
import ru.mirea.pasportnikovaeo.data.local.database.entity.UserStatsEntity;

@Database(
        entities = {UserEntity.class, BookEntity.class, UserStatsEntity.class},
        version = 2, // Увеличиваем версию, так как добавляем новую таблицу
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract BookDao bookDao();
    public abstract UserStatsDao userStatsDao(); // Добавляем этот метод

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "bookshelf_database"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}