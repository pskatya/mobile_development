package ru.mirea.pasportnikovaeo.data.model;

import ru.mirea.pasportnikovaeo.domain.model.Book;

public class BookDto {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private String externalLink;
    private Double price;
    private String currency;

    public BookDto(String id, String title, String author, String coverUrl,
                   String externalLink, Double price, String currency) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.externalLink = externalLink;
        this.price = price;
        this.currency = currency;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCoverUrl() { return coverUrl; }
    public String getExternalLink() { return externalLink; }
    public Double getPrice() { return price; }
    public String getCurrency() { return currency; }

    // Конвертация в Domain модель
    public Book toBook() {
        return new Book(id, title, author, coverUrl, externalLink, price, currency);
    }
}