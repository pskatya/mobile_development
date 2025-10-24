package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BookDetailViewModelFactory implements ViewModelProvider.Factory {
    private String bookId;

    public BookDetailViewModelFactory(String bookId) {
        this.bookId = bookId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookDetailViewModel.class)) {
            return (T) new BookDetailViewModel(bookId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}