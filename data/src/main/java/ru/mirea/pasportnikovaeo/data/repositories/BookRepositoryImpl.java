package ru.mirea.pasportnikovaeo.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.local.database.entity.BookEntity;
import ru.mirea.pasportnikovaeo.data.remote.api.NetworkApi;
import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.data.model.BookDto;
import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BookRepositoryImpl implements BookRepository {
    private NetworkApi networkApi;
    private AppDatabase database;
    private Random random = new Random();
    private static final String[] CATEGORIES = {"dark", "bright", "minimalist", "vintage"};

    // LiveData для разных источников данных
    private MutableLiveData<List<Book>> networkBooks = new MutableLiveData<>();
    private MutableLiveData<List<Book>> localBooks = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public BookRepositoryImpl(AppDatabase database) {
        this.networkApi = new NetworkApi();
        this.database = database;
    }

    private List<BookDto> createMockBooks() {
        List<BookDto> mockBooks = new ArrayList<>();

        // ✅ РЕАЛЬНЫЕ ССЫЛКИ НА AMAZON И ДРУГИЕ МАГАЗИНЫ
        mockBooks.add(new BookDto("1", "The Midnight Library", "Matt Haig",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1602190253i/52578297.jpg",
                "https://www.amazon.com/Midnight-Library-Novel-Matt-Haig/dp/0525559477", // Amazon
                9.99, "USD"));

        mockBooks.add(new BookDto("2", "Atomic Habits", "James Clear",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1655988385i/40121378.jpg",
                "https://www.amazon.com/Atomic-Habits-Proven-Build-Break/dp/0735211299", // Amazon
                11.99, "USD"));

        mockBooks.add(new BookDto("3", "The Alchemist", "Paulo Coelho",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1654371463i/18144590.jpg",
                "https://www.amazon.com/Alchemist-Paulo-Coelho/dp/0062315005", // Amazon
                8.99, "USD"));

        mockBooks.add(new BookDto("4", "1984", "George Orwell",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1657781256i/40961427.jpg",
                "https://www.amazon.com/1984-Signet-Classics-George-Orwell/dp/0451524934", // Amazon
                7.99, "USD"));

        mockBooks.add(new BookDto("5", "To Kill a Mockingbird", "Harper Lee",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1553383690i/2657.jpg",
                "https://www.amazon.com/Kill-Mockingbird-Harper-Lee/dp/0061120081", // Amazon
                10.99, "USD"));

        // ✅ ДОБАВИМ ЕЩЕ КНИГ С РАЗНЫМИ ССЫЛКАМИ
        mockBooks.add(new BookDto("6", "The Great Gatsby", "F. Scott Fitzgerald",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1490528560i/4671.jpg",
                "https://www.barnesandnoble.com/w/the-great-gatsby-f-scott-fitzgerald/1100042765", // Barnes & Noble
                6.99, "USD"));

        mockBooks.add(new BookDto("7", "Harry Potter and the Sorcerer's Stone", "J.K. Rowling",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1474154022i/3.jpg",
                "https://www.amazon.com/Harry-Potter-Sorcerers-Stone-Rowling/dp/059035342X", // Amazon
                12.99, "USD"));

        mockBooks.add(new BookDto("8", "Dune", "Frank Herbert",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1555447414i/44767458.jpg",
                "https://www.amazon.com/Dune-Frank-Herbert/dp/0441172717", // Amazon
                9.99, "USD"));

        mockBooks.add(new BookDto("9", "Pride and Prejudice", "Jane Austen",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1320399351i/1885.jpg",
                "https://www.gutenberg.org/ebooks/1342", // Project Gutenberg (бесплатно)
                0.0, "USD")); // Бесплатная книга

        mockBooks.add(new BookDto("10", "The Hobbit", "J.R.R. Tolkien",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1546071216i/5907.jpg",
                "https://www.amazon.com/Hobbit-J-R-Tolkien/dp/054792822X", // Amazon
                13.99, "USD"));

        return mockBooks;
    }

    @Override
    public void getBooks(BooksCallback callback) {
        isLoading.postValue(true);

        new Thread(() -> {
            try {
                List<BookDto> bookDtos = createMockBooks();
                List<Book> books = convertFromDtoToDomainBooks(bookDtos);

                // ВАЖНО: перед сохранением получаем текущие состояния лайков из БД
                List<BookEntity> existingBooks = database.bookDao().getAllBooks();
                Map<String, Boolean> existingLikes = new HashMap<>();
                for (BookEntity existingBook : existingBooks) {
                    existingLikes.put(existingBook.id, existingBook.isLiked);
                }

                // Обновляем состояния лайков в книгах
                for (Book book : books) {
                    if (existingLikes.containsKey(book.getId())) {
                        book.setLiked(existingLikes.get(book.getId()));
                    }
                }

                networkBooks.postValue(books);
                isLoading.postValue(false);
                callback.onSuccess(books);

                // Сохраняем книги с сохранением лайков
                saveBooksToLocalWithLikes(bookDtos, existingLikes);

            } catch (Exception e) {
                // Если нет сети, получаем из локальной БД
                try {
                    List<BookEntity> bookEntities = database.bookDao().getAllBooks();
                    List<Book> books = convertFromEntityToDomainBooks(bookEntities);

                    localBooks.postValue(books);
                    isLoading.postValue(false);
                    callback.onSuccess(books);

                } catch (Exception dbException) {
                    isLoading.postValue(false);
                    error.postValue("Failed to load books from all sources");
                    callback.onError(e);
                }
            }
        }).start();
    }

    @Override
    public void getLikedBooks(BooksCallback callback) {
        isLoading.postValue(true);

        new Thread(() -> {
            try {
                List<BookEntity> bookEntities = database.bookDao().getLikedBooks();
                List<Book> books = convertFromEntityToDomainBooks(bookEntities);

                isLoading.postValue(false);
                callback.onSuccess(books);

            } catch (Exception e) {
                isLoading.postValue(false);
                error.postValue("Failed to load liked books: " + e.getMessage());
                callback.onError(e);
            }
        }).start();
    }

    @Override
    public void toggleBookLike(String bookId, boolean isLiked) {
        new Thread(() -> {
            try {
                database.bookDao().updateBookLikeStatus(bookId, isLiked);
                System.out.println("Book " + bookId + " like status updated to: " + isLiked);
            } catch (Exception e) {
                error.postValue("Failed to update like status: " + e.getMessage());
            }
        }).start();
    }

    private List<Book> convertFromDtoToDomainBooks(List<BookDto> bookDtos) {
        List<Book> books = new ArrayList<>();
        for (BookDto dto : bookDtos) {
            books.add(dto.toBook());
        }
        return books;
    }

    private List<Book> convertFromEntityToDomainBooks(List<BookEntity> bookEntities) {
        List<Book> books = new ArrayList<>();
        for (BookEntity entity : bookEntities) {
            Book book = new Book(
                    entity.id,
                    entity.title,
                    entity.author,
                    entity.coverUrl,
                    entity.externalLink,
                    entity.price,
                    entity.currency
            );
            book.setLiked(entity.isLiked); // ВАЖНО: устанавливаем состояние лайка!
            books.add(book);
        }
        return books;
    }

    private void saveBooksToLocal(List<BookDto> bookDtos) {
        new Thread(() -> {
            try {
                List<BookEntity> bookEntities = new ArrayList<>();
                for (BookDto dto : bookDtos) {
                    BookEntity entity = new BookEntity(
                            dto.getId(),
                            dto.getTitle(),
                            dto.getAuthor(),
                            dto.getCoverUrl(),
                            dto.getExternalLink(),
                            dto.getPrice(),
                            dto.getCurrency(),
                            CATEGORIES[random.nextInt(CATEGORIES.length)],
                            System.currentTimeMillis(),
                            false
                    );
                    bookEntities.add(entity);
                }
                database.bookDao().insertBooks(bookEntities);
                System.out.println("Saved " + bookEntities.size() + " books to database");
            } catch (Exception e) {
                System.err.println("Error saving books to database: " + e.getMessage());
            }
        }).start();
    }

    private void saveBooksToLocalWithLikes(List<BookDto> bookDtos, Map<String, Boolean> existingLikes) {
        new Thread(() -> {
            try {
                List<BookEntity> bookEntities = new ArrayList<>();
                for (BookDto dto : bookDtos) {
                    // Сохраняем существующее состояние лайка или false по умолчанию
                    boolean isLiked = existingLikes.getOrDefault(dto.getId(), false);

                    BookEntity entity = new BookEntity(
                            dto.getId(),
                            dto.getTitle(),
                            dto.getAuthor(),
                            dto.getCoverUrl(),
                            dto.getExternalLink(),
                            dto.getPrice(),
                            dto.getCurrency(),
                            CATEGORIES[random.nextInt(CATEGORIES.length)],
                            System.currentTimeMillis(),
                            isLiked // Сохраняем лайк!
                    );
                    bookEntities.add(entity);
                }
                database.bookDao().insertBooks(bookEntities);
                System.out.println("Saved books with likes preserved");
            } catch (Exception e) {
                System.err.println("Error saving books: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void searchBooks(String query, BooksCallback callback) {
        isLoading.postValue(true);

        new Thread(() -> {
            try {
                List<BookDto> allBooks = createMockBooks();
                List<BookDto> filteredBooks = new ArrayList<>();
                for (BookDto book : allBooks) {
                    if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                }

                List<Book> books = convertFromDtoToDomainBooks(filteredBooks);
                isLoading.postValue(false);
                callback.onSuccess(books);

            } catch (Exception e) {
                // Если нет сети, ищем в локальной БД
                try {
                    List<BookEntity> bookEntities = database.bookDao().searchBooks(query);
                    List<Book> books = convertFromEntityToDomainBooks(bookEntities);

                    isLoading.postValue(false);
                    callback.onSuccess(books);

                } catch (Exception dbException) {
                    isLoading.postValue(false);
                    error.postValue("Search failed: " + e.getMessage());
                    callback.onError(e);
                }
            }
        }).start();
    }

    @Override
    public void getBookDetails(String bookId, BookDetailsCallback callback) {
        isLoading.postValue(true);

        new Thread(() -> {
            try {
                List<BookDto> allBooks = createMockBooks();
                BookDto foundBook = null;
                for (BookDto book : allBooks) {
                    if (book.getId().equals(bookId)) {
                        foundBook = book;
                        break;
                    }
                }

                if (foundBook != null) {
                    Book book = foundBook.toBook();
                    isLoading.postValue(false);
                    callback.onSuccess(book);
                } else {
                    isLoading.postValue(false);
                    callback.onError(new Exception("Book not found in network"));
                }

            } catch (Exception e) {
                // Если нет сети, ищем в локальной БД
                try {
                    BookEntity bookEntity = database.bookDao().getBookById(bookId);
                    if (bookEntity != null) {
                        Book book = new Book(
                                bookEntity.id,
                                bookEntity.title,
                                bookEntity.author,
                                bookEntity.coverUrl,
                                bookEntity.externalLink,
                                bookEntity.price,
                                bookEntity.currency
                        );
                        book.setLiked(bookEntity.isLiked);
                        isLoading.postValue(false);
                        callback.onSuccess(book);
                    } else {
                        isLoading.postValue(false);
                        callback.onError(new Exception("Book not found in local database"));
                    }

                } catch (Exception dbException) {
                    isLoading.postValue(false);
                    error.postValue("Failed to get book details: " + e.getMessage());
                    callback.onError(e);
                }
            }
        }).start();
    }

    // LiveData геттеры
    public LiveData<List<Book>> getNetworkBooksLiveData() {
        return networkBooks;
    }

    public LiveData<List<Book>> getLocalBooksLiveData() {
        return localBooks;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    public LiveData<String> getErrorLiveData() {
        return error;
    }
}