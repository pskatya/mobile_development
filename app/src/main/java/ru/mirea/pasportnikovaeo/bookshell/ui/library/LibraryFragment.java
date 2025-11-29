package ru.mirea.pasportnikovaeo.bookshell.ui.library;

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

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.FragmentLibraryBinding;
import ru.mirea.pasportnikovaeo.bookshell.ui.adapter.BookAdapter;
import ru.mirea.pasportnikovaeo.bookshell.ui.auth.AuthActivity;
import ru.mirea.pasportnikovaeo.bookshell.ui.detail.BookDetailActivity;
import ru.mirea.pasportnikovaeo.bookshell.ui.main.BookViewModel;
import ru.mirea.pasportnikovaeo.bookshell.ui.main.MainActivity;
import ru.mirea.pasportnikovaeo.data.repositories.AuthRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.repositories.AuthRepository;

public class LibraryFragment extends Fragment {
    private FragmentLibraryBinding binding;
    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;
    private AuthRepository authRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRepositories();
        checkAuthentication();
        initViewModel();
        setupUI();
        setupObservers();

        if (isUserLoggedIn()) {
            loadLikedBooks();
        }
    }

    private void initRepositories() {
        authRepository = new AuthRepositoryImpl(requireContext());
    }

    private boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }

    private void checkAuthentication() {
        if (!isUserLoggedIn()) {
            showLoginRequiredUI();
            return;
        }
    }

    private void initViewModel() {
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
    }

    private void setupUI() {
        // Настройка RecyclerView
        bookAdapter = new BookAdapter(requireContext());
        binding.recyclerViewLibrary.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerViewLibrary.setAdapter(bookAdapter);

        // Обработчик клика по книге - ПЕРЕХОД К ДЕТАЛЯМ
        bookAdapter.setOnBookClickListener(book -> {
            if (isUserLoggedIn()) {
                openBookDetails(book.getId());
            } else {
                showLoginRequiredMessage();
            }
        });

        // Обработчик лайков для библиотеки
        bookAdapter.setOnBookLikeListener((book, liked) -> {
            if (!isUserLoggedIn()) {
                showLoginRequiredMessage();
                return;
            }

            book.setLiked(liked);
            bookViewModel.toggleBookLike(book.getId(), liked);

            // Если сняли лайк - сразу обновляем список
            if (!liked) {
                loadLikedBooks();
                Toast.makeText(requireContext(), "Book removed from library", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка Discover Books
        binding.buttonDiscoverBooks.setOnClickListener(v -> {
            // Переходим на главный экран через bottom navigation
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.showHomeFragment();
                // Также обновляем выделение в bottom navigation
                mainActivity.binding.bottomNavigation.setSelectedItemId(R.id.navigation_home);
            }
        });

        // Кнопка Login для неавторизованных пользователей
        binding.buttonLoginRequired.setOnClickListener(v -> {
            navigateToAuth();
        });
    }

    private void showLoginRequiredUI() {
        binding.loginRequiredLayout.setVisibility(View.VISIBLE);
        binding.libraryContent.setVisibility(View.GONE);
    }

    private void showLibraryUI() {
        binding.loginRequiredLayout.setVisibility(View.GONE);
        binding.libraryContent.setVisibility(View.VISIBLE);
    }

    private void showLoginRequiredMessage() {
        Toast.makeText(requireContext(), "Please login to access library features", Toast.LENGTH_LONG).show();
        navigateToAuth();
    }

    private void navigateToAuth() {
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void openBookDetails(String bookId) {
        Intent intent = new Intent(requireContext(), BookDetailActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setupObservers() {
        // Наблюдаем за списком лайкнутых книг
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (isUserLoggedIn()) {
                if (books != null && !books.isEmpty()) {
                    bookAdapter.setBooks(books);
                    binding.emptyState.setVisibility(View.GONE);
                    binding.recyclerViewLibrary.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.recyclerViewLibrary.setVisibility(View.GONE);
                }
            }
        });

        // Наблюдаем за состоянием загрузки
        bookViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isUserLoggedIn()) {
                if (isLoading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.recyclerViewLibrary.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    if (bookViewModel.getBooks().getValue() != null &&
                            !bookViewModel.getBooks().getValue().isEmpty()) {
                        binding.recyclerViewLibrary.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Наблюдаем за ошибками
        bookViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadLikedBooks() {
        if (isUserLoggedIn()) {
            bookViewModel.loadLikedBooks();
            showLibraryUI();
        } else {
            showLoginRequiredUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // При возвращении на экран проверяем авторизацию
        if (isUserLoggedIn()) {
            loadLikedBooks();
        } else {
            showLoginRequiredUI();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}