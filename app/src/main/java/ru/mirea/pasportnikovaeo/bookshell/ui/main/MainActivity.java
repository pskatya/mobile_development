package ru.mirea.pasportnikovaeo.bookshell.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityMainBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.detail.BookDetailActivity;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.repositories.BookRepositoryImpl;
import ru.mirea.pasportnikovaeo.bookshell.ui.adapter.BookAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
        setupObservers();
        setupUI();
        // Убрал loadBooks() из onCreate - будет в onResume
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Загружаем книги когда активность становится видимой
        loadBooks();
    }

    private void initViewModel() {
        AppDatabase database = AppDatabase.getInstance(this);
        BookRepositoryImpl bookRepository = new BookRepositoryImpl(database);
        BookViewModelFactory factory = new BookViewModelFactory(bookRepository);
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
    }

    private void setupObservers() {
        // Наблюдаем за списком книг
        bookViewModel.getBooks().observe(this, books -> {
            if (books != null && !books.isEmpty()) {
                bookAdapter.setBooks(books);
                binding.emptyState.setVisibility(View.GONE);
                binding.booksRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyState.setVisibility(View.VISIBLE);
                binding.booksRecyclerView.setVisibility(View.GONE);
            }
        });

        // Наблюдаем за состоянием загрузки - просто показываем/скрываем список
        bookViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.booksRecyclerView.setVisibility(View.GONE);
            } else {
                binding.booksRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Наблюдаем за ошибками
        bookViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupUI() {
        // Настройка RecyclerView
        bookAdapter = new BookAdapter();
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.booksRecyclerView.setAdapter(bookAdapter);

        // Настройка поиска (у тебя EditText, а не SearchView)
        binding.searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.searchInput.getText().toString();
                if (!query.isEmpty()) {
                    bookViewModel.searchBooks(query);
                } else {
                    bookViewModel.loadBooks();
                }
                return true;
            }
            return false;
        });

        // Обработчик изменения текста для поиска в реальном времени
        binding.searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                String query = s.toString();
                if (query.isEmpty()) {
                    bookViewModel.loadBooks();
                }
            }
        });

        // Кнопка повтора
        binding.retryButton.setOnClickListener(v -> loadBooks());

        bookAdapter.setOnBookClickListener(book -> {
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            startActivity(intent);
        });
    }

    private void loadBooks() {
        bookViewModel.loadBooks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}