package roles;

import models.Book;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    private final int id;
    private final String name;
    private final List<Book> borrowedBooks = new ArrayList<>();

    public Reader(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("reader id must be positive");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("reader name is empty");
        }
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }

    public void addBook(Book book) { borrowedBooks.add(book); }

    public void removeBook(Book book) { borrowedBooks.remove(book); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader)) return false;
        Reader other = (Reader) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return name + " (id " + id + ")";
    }
}
