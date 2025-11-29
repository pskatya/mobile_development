package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;


public class BookDetailViewModel extends ViewModel {
    private String bookId;
    private BookRepository bookRepository; // ← ДОБАВИТЬ РЕПОЗИТОРИЙ

    private MutableLiveData<Book> book = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();

    // ✅ ИСПРАВЛЕННЫЙ КОНСТРУКТОР - добавляем репозиторий
    public BookDetailViewModel(String bookId, BookRepository bookRepository) {
        this.bookId = bookId;
        this.bookRepository = bookRepository;
    }

    public void loadBookDetails() {
        isLoading.setValue(true);
        error.setValue(null);

        // ✅ ИСПОЛЬЗУЕМ РЕПОЗИТОРИЙ ДЛЯ ЗАГРУЗКИ КОНКРЕТНОЙ КНИГИ
        bookRepository.getBookDetails(bookId, new BookRepository.BookDetailsCallback() {
            @Override
            public void onSuccess(Book loadedBook) {
                book.postValue(loadedBook);
                isLoading.postValue(false);
            }

            @Override
            public void onError(Exception e) {
                error.postValue("Failed to load book details: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    // ✅ УДАЛИТЬ старый метод createMockBook() - он всегда создает одну книгу

    public LiveData<Book> getBook() {
        return book;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }
}