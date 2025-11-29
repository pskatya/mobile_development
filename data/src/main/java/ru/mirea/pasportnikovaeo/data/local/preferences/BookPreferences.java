package ru.mirea.pasportnikovaeo.data.local.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class BookPreferences {
    private static final String PREFS_NAME = "book_preferences";
    private static final String KEY_LIKED_BOOKS = "liked_books";

    private SharedPreferences prefs;

    public BookPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getLikedBookIds() {
        return prefs.getStringSet(KEY_LIKED_BOOKS, new HashSet<>());
    }

    public void addLikedBook(String bookId) {
        Set<String> likedBooks = new HashSet<>(getLikedBookIds());
        likedBooks.add(bookId);
        prefs.edit().putStringSet(KEY_LIKED_BOOKS, likedBooks).apply();
    }

    public void removeLikedBook(String bookId) {
        Set<String> likedBooks = new HashSet<>(getLikedBookIds());
        likedBooks.remove(bookId);
        prefs.edit().putStringSet(KEY_LIKED_BOOKS, likedBooks).apply();
    }

    public boolean isBookLiked(String bookId) {
        return getLikedBookIds().contains(bookId);
    }
}