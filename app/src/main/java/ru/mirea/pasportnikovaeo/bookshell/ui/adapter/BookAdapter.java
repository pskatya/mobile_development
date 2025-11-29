package ru.mirea.pasportnikovaeo.bookshell.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.domain.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private OnBookClickListener onBookClickListener;
    private OnBookLikeListener onBookLikeListener;
    private Context context;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.onBookClickListener = listener;
    }

    public void setOnBookLikeListener(OnBookLikeListener listener) {
        this.onBookLikeListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        // Временное решение - используем поле isLiked из модели Book
        // В будущем это должно приходить из ViewModel
        boolean isLiked = book.isLiked(); // ← ДОБАВИТЬ этот метод в модель Book

        holder.bind(book, isLiked);

        // Клик на всю карточку
        holder.itemView.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(book);
            }
        });

        // Клик на кнопку лайка
        holder.likeButton.setOnClickListener(v -> {
            boolean newLikedState = !isLiked;

            // Уведомляем слушателя о изменении лайка
            if (onBookLikeListener != null) {
                onBookLikeListener.onBookLike(book, newLikedState);
            }

            // Мгновенное обновление UI
            holder.updateLikeButton(book, newLikedState);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookCover;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookRating;
        private TextView bookPrice;
        private ImageView externalLinkIcon;
        private ImageButton likeButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.book_cover);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookRating = itemView.findViewById(R.id.book_rating);
            bookPrice = itemView.findViewById(R.id.book_price);
            externalLinkIcon = itemView.findViewById(R.id.external_link_icon);
            likeButton = itemView.findViewById(R.id.like_button);
        }

        public void bind(Book book, boolean isLiked) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());

            // Рейтинг
            if (book.getRating() > 0) {
                bookRating.setText(String.format("%.1f", book.getRating()));
                bookRating.setVisibility(View.VISIBLE);
            } else {
                bookRating.setVisibility(View.GONE);
            }

            // Цена
            if (book.getPrice() > 0) {
                bookPrice.setText(String.format("$%.2f", book.getPrice()));
                bookPrice.setVisibility(View.VISIBLE);
            } else {
                bookPrice.setVisibility(View.GONE);
            }

            // Внешняя ссылка (если есть)
            if (book.getExternalLink() != null && !book.getExternalLink().isEmpty()) {
                externalLinkIcon.setVisibility(View.VISIBLE);
            } else {
                externalLinkIcon.setVisibility(View.INVISIBLE);
            }

            // Состояние кнопки лайка
            updateLikeButton(book, isLiked);

            // Загрузка изображения
            Glide.with(itemView.getContext())
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(bookCover);
        }

        public void updateLikeButton(Book book, boolean isLiked) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_filled);
                likeButton.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.red_500));
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outline);
                likeButton.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.md_theme_onSurface));
            }

            // Content description для доступности
            String contentDesc = isLiked ?
                    "Remove " + book.getTitle() + " from favorites" :
                    "Add " + book.getTitle() + " to favorites";
            likeButton.setContentDescription(contentDesc);
        }
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public interface OnBookLikeListener {
        void onBookLike(Book book, boolean liked);
    }
}