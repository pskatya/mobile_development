package ru.mirea.pasportnikovaeo.bookshell.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.bookshell.databinding.ActivityBookDetailBinding;
import ru.mirea.pasportnikovaeo.domain.model.Book;

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
        BookDetailViewModelFactory factory = new BookDetailViewModelFactory(bookId);
        bookDetailViewModel = new ViewModelProvider(this, factory).get(BookDetailViewModel.class);
    }

    private void setupUI() {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        bookDetailViewModel.getBook().observe(this, this::displayBookDetails);

        bookDetailViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        binding.readNowBtn.setOnClickListener(v -> {
            Book book = bookDetailViewModel.getBook().getValue();
            if (book != null && book.getExternalLink() != null && !book.getExternalLink().isEmpty()) {
                openExternalLink(book.getExternalLink());
            } else {
                Toast.makeText(this, "No external link available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addToLibraryBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Added to your library", Toast.LENGTH_SHORT).show();
        });

        binding.readMoreBtn.setOnClickListener(v -> toggleDescription());
    }

    private void displayBookDetails(Book book) {
        if (book == null) return;

        binding.toolbar.setTitle(book.getTitle());
        binding.bookTitle.setText(book.getTitle());
        binding.bookAuthor.setText(book.getAuthor());

        // Рейтинг
        if (book.getRating() != null && book.getRating() > 0) {
            binding.bookRating.setRating(book.getRating().floatValue());
            binding.ratingText.setText(String.format("%.1f/5", book.getRating()));
            // Можно добавить количество оценок
            binding.ratingCount.setText("(1,247 ratings)");
        } else {
            // Скрываем рейтинг если его нет
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

        // Цена
        if (book.getPrice() != null) {
            String priceText = formatPrice(book.getPrice(), book.getCurrency());
            binding.priceUsd.setText(priceText);
            binding.priceRub.setText("≈ " + convertToRub(book.getPrice()) + " RUB");
        } else {
            binding.priceUsd.setText("Free");
            binding.priceRub.setVisibility(View.GONE);
        }

        // Обложка
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Glide.with(this)
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(binding.bookCover);
        }

        // Дополнительная информация
        if (book.getPageCount() != null) {
            // Можно добавить отображение количества страниц
        }
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

    private String convertToRub(Double usdPrice) {
        if (usdPrice == null) return "0.00";
        double converted = usdPrice * 95.45;
        return String.format("%.2f", converted);
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
}