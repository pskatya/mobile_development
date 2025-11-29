package ru.mirea.pasportnikovaeo.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ru.mirea.pasportnikovaeo.data.local.database.entity.BookEntity;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    List<BookEntity> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :bookId")
    BookEntity getBookById(String bookId);

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    List<BookEntity> searchBooks(String query);

    @Query("SELECT * FROM books WHERE category = :category")
    List<BookEntity> getBooksByCategory(String category);

    // ДОБАВИТЬ: Получение только лайкнутых книг
    @Query("SELECT * FROM books WHERE isLiked = 1")
    List<BookEntity> getLikedBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(List<BookEntity> books);

    @Query("UPDATE books SET isLiked = :isLiked WHERE id = :bookId")
    void updateBookLikeStatus(String bookId, boolean isLiked);

    @Query("DELETE FROM books")
    void clearAllBooks();
}