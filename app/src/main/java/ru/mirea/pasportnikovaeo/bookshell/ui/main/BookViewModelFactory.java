package ru.mirea.pasportnikovaeo.bookshell.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

public class BookViewModelFactory implements ViewModelProvider.Factory {
    private BookRepository bookRepository;

    public BookViewModelFactory(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookViewModel.class)) {
            return (T) new BookViewModel(bookRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}