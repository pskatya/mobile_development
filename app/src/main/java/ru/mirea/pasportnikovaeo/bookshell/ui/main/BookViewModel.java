package ru.mirea.pasportnikovaeo.bookshell.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {
    private BookRepository bookRepository;

    // MediatorLiveData для объединения данных из сети и БД
    private MediatorLiveData<List<Book>> booksMediator = new MediatorLiveData<>();

    // Источники данных
    private MutableLiveData<List<Book>> networkBooks = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Book>> localBooks = new MutableLiveData<>(new ArrayList<>());

    // Состояние UI
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();

    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        setupMediatorLiveData();
    }

    private void setupMediatorLiveData() {
        // Настраиваем MediatorLiveData для объединения данных
        booksMediator.addSource(networkBooks, books -> {
            if (books != null && !books.isEmpty()) {
                // Приоритет у данных из сети
                booksMediator.setValue(books); // ОК - вызывается из главного потока
                // Сохраняем в локальные для кэширования
                localBooks.setValue(books); // ОК - вызывается из главного потока
            } else if (localBooks.getValue() != null && !localBooks.getValue().isEmpty()) {
                // Если нет сетевых данных, используем локальные
                booksMediator.setValue(localBooks.getValue()); // ОК - вызывается из главного потока
            } else {
                // Нет данных вообще
                booksMediator.setValue(new ArrayList<>()); // ОК - вызывается из главного потока
            }
        });

        booksMediator.addSource(localBooks, books -> {
            // Если нет сетевых данных, используем локальные
            if (networkBooks.getValue() == null || networkBooks.getValue().isEmpty()) {
                if (books != null && !books.isEmpty()) {
                    booksMediator.setValue(books); // ОК - вызывается из главного потока
                }
            }
        });

        // Инициализируем пустым списком
        booksMediator.setValue(new ArrayList<>()); // ОК - вызывается из главного потока
    }

    public void loadBooks() {
        // ИСПРАВЛЕНО: postValue вместо setValue
        isLoading.postValue(true);
        error.postValue(null);

        // Загружаем из сети
        bookRepository.getBooks(new BookRepository.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                networkBooks.postValue(books);
            }

            @Override
            public void onError(Exception e) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                error.postValue("Network error: " + e.getMessage());

                // Пробуем загрузить из локальной БД
                loadFromLocalDatabase();
            }
        });
    }

    private void loadFromLocalDatabase() {
        // TODO: Реализовать загрузку из локальной БД
        List<Book> cachedBooks = getCachedBooks();
        if (cachedBooks != null && !cachedBooks.isEmpty()) {
            // ИСПРАВЛЕНО: postValue вместо setValue
            localBooks.postValue(cachedBooks);
            error.postValue("Using cached data");
        } else {
            // ИСПРАВЛЕНО: postValue вместо setValue
            error.postValue("No data available");
        }
    }

    private List<Book> getCachedBooks() {
        // TODO: Заменить на реальную загрузку из БД
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("1", "Book 1", "Author 1",
                "https://example.com/cover1.jpg", "https://example.com", 9.99, "USD"));
        mockBooks.add(new Book("2", "Book 2", "Author 2",
                "https://example.com/cover2.jpg", "https://example.com", 12.99, "USD"));
        return mockBooks;
    }

    public void searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadBooks();
            return;
        }

        // ИСПРАВЛЕНО: postValue вместо setValue
        isLoading.postValue(true);

        bookRepository.searchBooks(query.trim(), new BookRepository.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                // Для поиска используем напрямую результат
                // ИСПРАВЛЕНО: postValue вместо setValue
                booksMediator.postValue(books);
            }

            @Override
            public void onError(Exception e) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                error.postValue("Search failed: " + e.getMessage());
            }
        });
    }

    public void loadLikedBooks() {
        // ИСПРАВЛЕНО: postValue вместо setValue
        isLoading.postValue(true);
        error.postValue(null);

        bookRepository.getLikedBooks(new BookRepository.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                // Для лайкнутых книг используем напрямую результат
                // ИСПРАВЛЕНО: postValue вместо setValue
                booksMediator.postValue(books);
            }

            @Override
            public void onError(Exception e) {
                // ИСПРАВЛЕНО: postValue вместо setValue
                isLoading.postValue(false);
                error.postValue("Failed to load liked books: " + e.getMessage());
            }
        });
    }

    public void toggleBookLike(String bookId, boolean isLiked) {
        bookRepository.toggleBookLike(bookId, isLiked);

        // Обновляем локальные данные
        List<Book> currentBooks = booksMediator.getValue();
        if (currentBooks != null) {
            for (Book book : currentBooks) {
                if (book.getId().equals(bookId)) {
                    // Обновляем состояние книги в списке
                    book.setLiked(isLiked);
                    break;
                }
            }
            // ИСПРАВЛЕНО: postValue вместо setValue
            booksMediator.postValue(new ArrayList<>(currentBooks));
        }
    }

    // Геттеры
    public LiveData<List<Book>> getBooks() {
        return booksMediator;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }
}