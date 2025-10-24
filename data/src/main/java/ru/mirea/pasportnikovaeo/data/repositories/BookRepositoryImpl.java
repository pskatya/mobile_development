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
import java.util.List;
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

    @Override
    public void getBooks(BooksCallback callback) {
        isLoading.setValue(true);

        new Thread(() -> {
            try {
                // Пробуем получить данные из сети
                List<BookDto> bookDtos = networkApi.getBooks();
                List<Book> books = convertFromDtoToDomainBooks(bookDtos);

                networkBooks.postValue(books);
                isLoading.postValue(false);
                callback.onSuccess(books);

                // Сохраняем в локальную БД
                saveBooksToLocal(bookDtos);

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
            books.add(new Book(
                    entity.id,
                    entity.title,
                    entity.author,
                    entity.coverUrl,
                    entity.externalLink,
                    entity.price,
                    entity.currency
            ));
        }
        return books;
    }

    private void saveBooksToLocal(List<BookDto> bookDtos) {
        new Thread(() -> {
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
                        System.currentTimeMillis()
                );
                bookEntities.add(entity);
            }
            database.bookDao().insertBooks(bookEntities);
        }).start();
    }

    @Override
    public void searchBooks(String query, BooksCallback callback) {
        isLoading.setValue(true);

        new Thread(() -> {
            try {
                // Пробуем поиск в сети
                List<BookDto> bookDtos = networkApi.searchBooks(query);
                List<Book> books = convertFromDtoToDomainBooks(bookDtos);

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
        isLoading.setValue(true);

        new Thread(() -> {
            try {
                // Пробуем получить из сети
                BookDto bookDto = networkApi.getBookDetails(bookId);
                if (bookDto != null) {
                    Book book = bookDto.toBook();
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

    // LiveData геттеры для использования в ViewModel с MediatorLiveData
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