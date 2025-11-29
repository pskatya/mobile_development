package ru.mirea.pasportnikovaeo.domain.repositories;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import java.util.List;

public interface LibraryRepository {
    interface LibraryCallback {
        void onSuccess(List<Book> books);
        void onError(Exception e);
    }

    void getLibraryBooks(LibraryCallback callback);
    void addToLibrary(Book book, LibraryCallback callback);
    void removeFromLibrary(String bookId, LibraryCallback callback);
    boolean isBookInLibrary(String bookId);
}