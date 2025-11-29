package ru.mirea.pasportnikovaeo.domain.repositories;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import java.util.List;

public interface BookRepository {
    interface BooksCallback {
        void onSuccess(List<Book> books);
        void onError(Exception e);
    }

    interface BookDetailsCallback {
        void onSuccess(Book book);
        void onError(Exception e);
    }

    void getBooks(BooksCallback callback);
    void searchBooks(String query, BooksCallback callback);
    void getBookDetails(String bookId, BookDetailsCallback callback);

    // ДОБАВИТЬ эти методы:
    void getLikedBooks(BooksCallback callback);
    void toggleBookLike(String bookId, boolean isLiked);
}