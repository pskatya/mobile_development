package ru.mirea.pasportnikovaeo.bookshell.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ru.mirea.pasportnikovaeo.bookshell.R;
import ru.mirea.pasportnikovaeo.domain.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private OnBookClickListener onBookClickListener;

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.onBookClickListener = listener;
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
        holder.bind(book);

        holder.itemView.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(book);
            }
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

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.book_cover);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
        }

        public void bind(Book book) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());

            // Загрузка изображения с помощью Glide
            Glide.with(itemView.getContext())
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(bookCover);
        }
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
}
