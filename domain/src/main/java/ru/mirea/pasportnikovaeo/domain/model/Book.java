package ru.mirea.pasportnikovaeo.domain.model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private String externalLink;
    private Double price;
    private String currency;
    private String description;
    private Double rating;
    private String category;
    private Integer pageCount;
    private String publishedDate;
    private boolean liked; // ← ДОБАВИТЬ это поле

    // Конструктор для существующих случаев
    public Book(String id, String title, String author, String coverUrl,
                String externalLink, Double price, String currency) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.externalLink = externalLink;
        this.price = price;
        this.currency = currency;
        this.description = "";
        this.rating = 0.0;
        this.category = "";
        this.liked = false; // ← Инициализировать
    }

    // Полный конструктор
    public Book(String id, String title, String author, String coverUrl,
                String externalLink, Double price, String currency,
                String description, Double rating, String category,
                Integer pageCount, String publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.externalLink = externalLink;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.rating = rating;
        this.category = category;
        this.pageCount = pageCount;
        this.publishedDate = publishedDate;
        this.liked = false; // ← Инициализировать
    }

    // Конструктор с liked
    public Book(String id, String title, String author, String coverUrl,
                String externalLink, Double price, String currency,
                String description, Double rating, String category,
                Integer pageCount, String publishedDate, boolean liked) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.externalLink = externalLink;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.rating = rating;
        this.category = category;
        this.pageCount = pageCount;
        this.publishedDate = publishedDate;
        this.liked = liked;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCoverUrl() { return coverUrl; }
    public String getExternalLink() { return externalLink; }
    public Double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public Double getRating() { return rating; }
    public String getCategory() { return category; }
    public Integer getPageCount() { return pageCount; }
    public String getPublishedDate() { return publishedDate; }
    public boolean isLiked() { return liked; } // ← ДОБАВИТЬ этот геттер

    // Setters
    public void setDescription(String description) { this.description = description; }
    public void setRating(Double rating) { this.rating = rating; }
    public void setCategory(String category) { this.category = category; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public void setLiked(boolean liked) { this.liked = liked; } // ← ДОБАВИТЬ этот сеттер
}