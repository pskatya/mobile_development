package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.domain.model.Book;

public class BookDetailViewModel extends ViewModel {
    private String bookId;

    private MutableLiveData<Book> book = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();

    public BookDetailViewModel(String bookId) {
        this.bookId = bookId;
    }

    public void loadBookDetails() {
        isLoading.setValue(true);
        error.setValue(null);

        new Thread(() -> {
            try {
                Thread.sleep(500);
                Book mockBook = createMockBook();
                book.postValue(mockBook);
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                isLoading.postValue(false);
                error.postValue("Failed to load book details");
            }
        }).start();
    }

    private Book createMockBook() {
        return new Book(
                bookId,
                "The Midnight Library",
                "Matt Haig",
                "https://example.com/cover.jpg",
                "https://www.amazon.com/Midnight-Library-Novel-Matt-Haig/dp/0525559477",
                9.99,
                "USD",
                "Between life and death there is a library, and within that library, the shelves go on forever. Every book provides a chance to try another life you could have lived. To see how things would be if you had made other choices... Would you have done anything different, if you had the chance to undo your regrets?",
                4.2,
                "Fiction",
                304,
                "2020"
        );
    }

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