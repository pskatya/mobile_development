package ru.mirea.pasportnikovaeo.recyclerviewapp;

public class Event {
    private String title;
    private String description;
    private String imageName;
    private int year;

    public Event(String title, String description, String imageName, int year) {
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public int getYear() {
        return year;
    }
}