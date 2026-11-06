package models;

public abstract class Book implements Comparable<Book> {
    private final int id;
    private String title;
    private String author;
    private int year;
    private double price;

    public Book(int id, String title, String author, int year, double price) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        this.id = id;
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPrice(price);
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title is empty");
        }
        this.title = title;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("author is empty");
        }
        this.author = author;
    }

    public int getYear() { return year; }
    public void setYear(int year) {
        if (year < 1000 || year > 2026) {
            throw new IllegalArgumentException("wrong year: " + year);
        }
        this.year = year;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
    }

    public abstract String getCategory();
    public abstract int getLoanPeriodDays();
    public abstract double calculateLateFee(int daysLate);

    public void displayInfo() {
        System.out.println("[" + getCategory() + "] ID: " + id + ", Title: " + title
                + ", Author: " + author + ", Year: " + year + ", Price: $" + price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book other = (Book) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public int compareTo(Book other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "#" + id + " " + title;
    }
}
