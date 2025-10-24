package ru.mirea.pasportnikovaeo.domain.usecase;

import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

public class GetBooksUseCase {
    private BookRepository bookRepository;

    public GetBooksUseCase(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void execute(BookRepository.BooksCallback callback) {
        bookRepository.getBooks(callback);
    }
}

