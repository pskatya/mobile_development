package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityBookDetailBinding;
import ru.mirea.pasportnikovaeo.bookshell.utils.CurrencyUtils;
import ru.mirea.pasportnikovaeo.data.local.database.AppDatabase;
import ru.mirea.pasportnikovaeo.data.repositories.BookRepositoryImpl;
import ru.mirea.pasportnikovaeo.domain.model.Book;
import ru.mirea.pasportnikovaeo.domain.repositories.BookRepository;

public class BookDetailActivity extends AppCompatActivity {

    private ActivityBookDetailBinding binding;
    private BookDetailViewModel bookDetailViewModel;
    private String bookId;
    private boolean isDescriptionExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookId = getIntent().getStringExtra("BOOK_ID");

        Log.d("BookDetailActivity", "Received bookId: " + bookId);

        if (bookId == null) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViewModel();
        setupUI();
        loadBookDetails();
    }

    private void initViewModel() {
        AppDatabase database = AppDatabase.getInstance(this);
        BookRepository bookRepository = new BookRepositoryImpl(database);
        BookDetailViewModelFactory factory = new BookDetailViewModelFactory(bookId, bookRepository);
        bookDetailViewModel = new ViewModelProvider(this, factory).get(BookDetailViewModel.class);
    }

    private void setupUI() {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Наблюдаем за данными книги
        bookDetailViewModel.getBook().observe(this, this::displayBookDetails);

        // Наблюдаем за состоянием загрузки - УПРОЩЕННАЯ ВЕРСИЯ
        bookDetailViewModel.getIsLoading().observe(this, isLoading -> {
            // Просто показываем/скрываем основной контент
            if (isLoading != null && isLoading) {
                binding.bookTitle.setVisibility(View.GONE);
                binding.bookAuthor.setVisibility(View.GONE);
            } else {
                binding.bookTitle.setVisibility(View.VISIBLE);
                binding.bookAuthor.setVisibility(View.VISIBLE);
            }
        });

        // Наблюдаем за ошибками
        bookDetailViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Кнопка "Read Now"
        binding.readNowBtn.setOnClickListener(v -> {
            Book book = bookDetailViewModel.getBook().getValue();
            if (book != null && book.getExternalLink() != null && !book.getExternalLink().isEmpty()) {
                openExternalLink(book.getExternalLink());
            } else {
                Toast.makeText(this, "No external link available", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Read More/Less" для описания
        binding.readMoreBtn.setOnClickListener(v -> toggleDescription());
    }

    private void displayBookDetails(Book book) {
        if (book == null) return;

        Log.d("BookDetailActivity", "Displaying book: " + book.getTitle() + " (ID: " + book.getId() + ")");

        binding.toolbar.setTitle(book.getTitle());
        binding.bookTitle.setText(book.getTitle());
        binding.bookAuthor.setText(book.getAuthor());

        if (book.getRating() != null && book.getRating() > 0) {
            binding.bookRating.setRating(book.getRating().floatValue());
            binding.ratingText.setText(String.format("%.1f/5", book.getRating()));
            binding.ratingCount.setText("(1,247 ratings)");
        } else {
            binding.bookRating.setVisibility(View.GONE);
            binding.ratingText.setVisibility(View.GONE);
            binding.ratingCount.setVisibility(View.GONE);
        }

        // Описание
        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            binding.bookDescription.setText(book.getDescription());
            setupDescriptionExpansion(book.getDescription());
        } else {
            binding.bookDescription.setText("No description available");
            binding.readMoreBtn.setVisibility(View.GONE);
        }

        if (book.getPrice() != null && book.getPrice() > 0) {
            String priceText = formatPrice(book.getPrice(), book.getCurrency());
            binding.priceUsd.setText(priceText);

            String priceRub = CurrencyUtils.formatPriceInRub(this, book.getPrice());
            binding.priceRub.setText("≈ " + priceRub);
            // Просто показываем цену
        } else {
            binding.priceUsd.setText("Free");
            binding.priceRub.setVisibility(View.GONE);
        }

        // Обложка с Glide
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Glide.with(this)
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .centerCrop()
                    .into(binding.bookCover);
        } else {
            binding.bookCover.setImageResource(R.drawable.ic_book_placeholder);
        }

        // Дополнительная информация - УПРОЩЕННАЯ ВЕРСИЯ
        // Если у тебя нет этих полей в layout, просто пропускаем
        // pageCount, publishedDate, genre и т.д.
    }

    private String formatPrice(Double price, String currency) {
        if (currency != null) {
            switch (currency) {
                case "USD":
                    return String.format("$%.2f", price);
                case "EUR":
                    return String.format("€%.2f", price);
                case "RUB":
                    return String.format("%.2f RUB", price);
                default:
                    return String.format("%.2f %s", price, currency);
            }
        }
        return String.format("%.2f", price);
    }

    private void setupDescriptionExpansion(String description) {
        binding.bookDescription.post(() -> {
            int lineCount = binding.bookDescription.getLineCount();
            if (lineCount > 3) {
                binding.readMoreBtn.setVisibility(View.VISIBLE);
                binding.bookDescription.setMaxLines(3);
                isDescriptionExpanded = false;
                binding.readMoreBtn.setText("Read more");
            } else {
                binding.readMoreBtn.setVisibility(View.GONE);
            }
        });
    }

    private void toggleDescription() {
        if (isDescriptionExpanded) {
            binding.bookDescription.setMaxLines(3);
            binding.readMoreBtn.setText("Read more");
        } else {
            binding.bookDescription.setMaxLines(Integer.MAX_VALUE);
            binding.readMoreBtn.setText("Read less");
        }
        isDescriptionExpanded = !isDescriptionExpanded;
    }

    private void openExternalLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open link", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBookDetails() {
        bookDetailViewModel.loadBookDetails();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}