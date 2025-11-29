package ru.mirea.pasportnikovaeo.data.repositories;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.LibraryRepository;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepositoryImpl implements LibraryRepository {
    // Временное хранилище (замени на Room/SQLite)
    private List<Book> libraryBooks = new ArrayList<>();

    @Override
    public void getLibraryBooks(LibraryCallback callback) {
        // Имитация загрузки из БД
        new Thread(() -> {
            try {
                Thread.sleep(500);

                if (libraryBooks.isEmpty()) {
                    // Если библиотека пуста, создаем мок-данные
                    createMockLibraryBooks();
                }

                callback.onSuccess(new ArrayList<>(libraryBooks));

            } catch (InterruptedException e) {
                callback.onError(new Exception("Failed to load library"));
            }
        }).start();
    }

    @Override
    public void addToLibrary(Book book, LibraryCallback callback) {
        if (!isBookInLibrary(book.getId())) {
            libraryBooks.add(book);
        }
        callback.onSuccess(new ArrayList<>(libraryBooks));
    }

    @Override
    public void removeFromLibrary(String bookId, LibraryCallback callback) {
        libraryBooks.removeIf(book -> book.getId().equals(bookId));
        callback.onSuccess(new ArrayList<>(libraryBooks));
    }

    @Override
    public boolean isBookInLibrary(String bookId) {
        return libraryBooks.stream().anyMatch(book -> book.getId().equals(bookId));
    }

    private void createMockLibraryBooks() {
        // Тестовые книги для библиотеки
        libraryBooks.add(new Book(
                "1", "The Midnight Library", "Matt Haig",
                "https://example.com/cover1.jpg", "https://example.com/book1",
                9.99, "USD", "Between life and death there is a library...", 4.5, "Fiction", 304, "2020"
        ));

        libraryBooks.add(new Book(
                "2", "Atomic Habits", "James Clear",
                "https://example.com/cover2.jpg", "https://example.com/book2",
                11.99, "USD", "Tiny Changes, Remarkable Results...", 4.8, "Self-Help", 320, "2018"
        ));

        libraryBooks.add(new Book(
                "3", "The Alchemist", "Paulo Coelho",
                "https://example.com/cover3.jpg", "https://example.com/book3",
                8.99, "USD", "A magical tale about following your dreams...", 4.7, "Fiction", 208, "1988"
        ));
    }
}