package models;

import roles.Reader;

public class BorrowRequest {
    private final int id;
    private final Reader reader;
    private final Book book;

    public BorrowRequest(int id, Reader reader, Book book) {
        this.id = id;
        this.reader = reader;
        this.book = book;
    }

    public int getId() { return id; }
    public Reader getReader() { return reader; }
    public Book getBook() { return book; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowRequest)) return false;
        BorrowRequest other = (BorrowRequest) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Request #" + id + ": " + reader.getName() + " wants \"" + book.getTitle() + "\"";
    }
}
