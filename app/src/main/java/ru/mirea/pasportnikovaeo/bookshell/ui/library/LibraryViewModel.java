package ru.mirea.pasportnikovaeo.bookshell.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.pasportnikovaeo.data.repositories.LibraryRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.LibraryRepository;

import java.util.List;

public class LibraryViewModel extends ViewModel {
    private LibraryRepository libraryRepository;

    private MutableLiveData<List<Book>> libraryBooks = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LibraryViewModel() {
        this.libraryRepository = new LibraryRepositoryImpl();
    }

    public void loadLibraryBooks() {
        isLoading.setValue(true);
        error.setValue(null);

        libraryRepository.getLibraryBooks(new LibraryRepository.LibraryCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                isLoading.postValue(false);
                libraryBooks.postValue(books);
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                error.postValue("Failed to load library: " + e.getMessage());
            }
        });
    }

    public void removeFromLibrary(String bookId) {
        libraryRepository.removeFromLibrary(bookId, new LibraryRepository.LibraryCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                libraryBooks.postValue(books);
            }

            @Override
            public void onError(Exception e) {
                error.postValue("Failed to remove book: " + e.getMessage());
            }
        });
    }

    // Геттеры
    public LiveData<List<Book>> getLibraryBooks() {
        return libraryBooks;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }
}