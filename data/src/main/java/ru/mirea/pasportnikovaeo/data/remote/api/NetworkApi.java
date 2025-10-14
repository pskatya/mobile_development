package ru.mirea.pasportnikovaeo.data.remote.api;

import ru.mirea.pasportnikovaeo.data.model.BookDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkApi {

    private static final String[] BOOK_TITLES = {
            "The Midnight Library", "1984", "The Alchemist", "Dune",
            "Atomic Habits", "The Psychology of Money", "Pride and Prejudice",
            "To Kill a Mockingbird", "The Great Gatsby", "Harry Potter"
    };

    private static final String[] AUTHORS = {
            "Matt Haig", "George Orwell", "Paulo Coelho", "Frank Herbert",
            "James Clear", "Morgan Housel", "Jane Austen", "Harper Lee",
            "F. Scott Fitzgerald", "J.K. Rowling"
    };

    private static final String[] CATEGORIES = {"dark", "bright", "minimalist", "vintage"};

    private Random random = new Random();

    public List<BookDto> getBooks() {
        // Имитация сетевой задержки
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            books.add(createMockBook(i, category));
        }

        return books;
    }

    public List<BookDto> searchBooks(String query) {
        // Имитация сетевой задержки
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<BookDto> allBooks = getBooks();
        List<BookDto> results = new ArrayList<>();

        for (BookDto book : allBooks) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            }
        }

        return results;
    }

    public BookDto getBookDetails(String bookId) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<BookDto> allBooks = getBooks();
        for (BookDto book : allBooks) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }

        return null;
    }

    private BookDto createMockBook(int id, String category) {
        String title = BOOK_TITLES[id % BOOK_TITLES.length];
        String author = AUTHORS[id % AUTHORS.length];

        return new BookDto(
                "book_" + id,
                title,
                author,
                "https://example.com/covers/" + category + "_" + id + ".jpg",
                "https://example.com/books/" + id,
                random.nextDouble() * 50 + 10, // цена от 10 до 60
                random.nextBoolean() ? "USD" : "RUB"
                // Убрали category - он не нужен в BookDto
        );
    }
}