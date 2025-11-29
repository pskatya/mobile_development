package ru.mirea.pasportnikovaeo.domain.model;

public class UserStats {
    private String userId;
    private int booksRead;
    private int booksLiked;
    private long memberSince;
    private long lastUpdated;

    public UserStats(String userId, int booksRead, int booksLiked, long memberSince) {
        this.userId = userId;
        this.booksRead = booksRead;
        this.booksLiked = booksLiked;
        this.memberSince = memberSince;
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters
    public String getUserId() { return userId; }
    public int getBooksRead() { return booksRead; }
    public int getBooksLiked() { return booksLiked; }
    public long getMemberSince() { return memberSince; }
    public long getLastUpdated() { return lastUpdated; }
}