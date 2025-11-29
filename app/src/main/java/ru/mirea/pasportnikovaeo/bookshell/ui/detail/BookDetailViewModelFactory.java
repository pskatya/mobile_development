package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

public class BookDetailViewModelFactory implements ViewModelProvider.Factory {
    private String bookId;
    private BookRepository bookRepository; // ← ДОБАВИТЬ РЕПОЗИТОРИЙ

    // ✅ ИСПРАВЛЕННЫЙ КОНСТРУКТОР
    public BookDetailViewModelFactory(String bookId, BookRepository bookRepository) {
        this.bookId = bookId;
        this.bookRepository = bookRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookDetailViewModel.class)) {
            // ✅ ПЕРЕДАЕМ РЕПОЗИТОРИЙ В ViewModel
            return (T) new BookDetailViewModel(bookId, bookRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}