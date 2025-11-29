package ru.mirea.pasportnikovaeo.data.local.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String author;
    public String coverUrl;
    public String externalLink;
    public Double price;
    public String currency;
    public String category;
    public long addedDate;
    public boolean isLiked; // ← ДОБАВИТЬ это поле

    public BookEntity(@NonNull String id, String title, String author, String coverUrl,
                      String externalLink, Double price, String currency,
                      String category, long addedDate, boolean isLiked) { // ← ДОБАВИТЬ параметр
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.externalLink = externalLink;
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.addedDate = addedDate;
        this.isLiked = isLiked; // ← Инициализировать
    }
}