package ru.mirea.pasportnikovaeo.bookshell.ui.main;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;

import ru.mirea.pasportnikovaeo.bookshell.databinding.FragmentHomeBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.adapter.BookAdapter;
import ru.mirea.pasportnikovaeo.bookshell.ui.detail.BookDetailActivity;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.repositories.BookRepositoryImpl;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        setupUI();
        setupObservers();
        loadBooks();
    }

    private void initViewModel() {
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
    }

    private void setupUI() {
        // Настройка RecyclerView
        bookAdapter = new BookAdapter(requireContext());
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.booksRecyclerView.setAdapter(bookAdapter);

        // Настройка поиска
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

        // Обработчик клика по книге - переход к деталям
        bookAdapter.setOnBookClickListener(book -> {
            openBookDetails(book.getId());
        });

        // Обработчик лайков
        bookAdapter.setOnBookLikeListener((book, liked) -> {
            book.setLiked(liked);
            bookViewModel.toggleBookLike(book.getId(), liked);
            Toast.makeText(requireContext(),
                    liked ? "Book added to library" : "Book removed from library",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void openBookDetails(String bookId) {
        Intent intent = new Intent(requireContext(), BookDetailActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setupObservers() {
        // Наблюдаем за списком книг
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                bookAdapter.setBooks(books);
                binding.emptyState.setVisibility(View.GONE);
                binding.booksRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyState.setVisibility(View.VISIBLE);
                binding.booksRecyclerView.setVisibility(View.GONE);
            }
        });

        // Наблюдаем за состоянием загрузки
        bookViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.booksRecyclerView.setVisibility(View.GONE);
            } else {
                binding.booksRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Наблюдаем за ошибками
        bookViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadBooks() {
        bookViewModel.loadBooks();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}