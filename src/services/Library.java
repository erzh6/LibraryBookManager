package services;

import exceptions.BookNotFoundException;
import interfaces.Borrowable;
import interfaces.Downloadable;
import models.Book;
import models.BorrowRequest;
import roles.Reader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class Library {
    private List<Book> books = new ArrayList<>();
    private Map<Integer, Book> booksById = new HashMap<>();
    private Map<String, List<Book>> booksByCategory = new HashMap<>();
    private Map<Integer, Reader> readersById = new HashMap<>();
    private Queue<BorrowRequest> requests = new ArrayDeque<>();
    private Map<Integer, Set<Reader>> borrowHistory = new HashMap<>();
    private int nextRequestId = 1;

    public void addBook(Book book) {
        if (book == null) {
            System.out.println("Error: book is null");
            return;
        }
        if (booksById.containsKey(book.getId())) {
            System.out.println("Error: book with id " + book.getId() + " already exists");
            return;
        }
        books.add(book);
        booksById.put(book.getId(), book);

        String category = book.getCategory();
        if (!booksByCategory.containsKey(category)) {
            booksByCategory.put(category, new ArrayList<>());
        }
        booksByCategory.get(category).add(book);

        System.out.println("Added: " + book);
    }

    public Book findBookById(int id) {
        return booksById.get(id);
    }

    public void updateBook(int id, String newTitle, double newPrice) {
        Book book = findBookById(id);
        if (book == null) {
            System.out.println("Error: book with id " + id + " not found");
            return;
        }
        if (newTitle == null || newTitle.isEmpty()) {
            System.out.println("Error: title is empty, book was not changed");
            return;
        }
        if (newPrice < 0) {
            System.out.println("Error: price cannot be negative, book was not changed");
            return;
        }
        book.setTitle(newTitle);
        book.setPrice(newPrice);
        System.out.println("Updated: " + book);
    }

    public void removeBook(int id) throws BookNotFoundException {
        Book book = findBookById(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }

        books.remove(book);
        booksById.remove(id);

        List<Book> categoryList = booksByCategory.get(book.getCategory());
        categoryList.remove(book);
        if (categoryList.isEmpty()) {
            booksByCategory.remove(book.getCategory());
        }

        for (Reader reader : readersById.values()) {
            reader.removeBook(book);
        }

        borrowHistory.remove(id);

        Queue<BorrowRequest> newRequests = new ArrayDeque<>();
        for (BorrowRequest request : requests) {
            if (!request.getBook().equals(book)) {
                newRequests.offer(request);
            }
        }
        requests = newRequests;

        System.out.println("Removed: " + book);
    }

    public void addReader(Reader reader) {
        if (readersById.containsKey(reader.getId())) {
            System.out.println("Error: reader with id " + reader.getId() + " already exists");
            return;
        }
        readersById.put(reader.getId(), reader);
        System.out.println("Registered reader: " + reader);
    }

    public Reader findReaderById(int id) {
        return readersById.get(id);
    }

    public void submitRequest(int readerId, int bookId) {
        Reader reader = findReaderById(readerId);
        Book book = findBookById(bookId);
        if (reader == null) {
            System.out.println("Error: reader " + readerId + " not found");
            return;
        }
        if (book == null) {
            System.out.println("Error: book " + bookId + " not found");
            return;
        }
        BorrowRequest request = new BorrowRequest(nextRequestId, reader, book);
        nextRequestId++;
        requests.offer(request);
        System.out.println("Submitted " + request);
    }

    public boolean hasRequests() {
        return !requests.isEmpty();
    }

    public void processNextRequest() {
        BorrowRequest request = requests.poll();
        if (request == null) {
            System.out.println("No requests in queue");
            return;
        }

        System.out.println("Processing " + request);
        Book book = request.getBook();
        Reader reader = request.getReader();

        if (!(book instanceof Borrowable)) {
            if (book instanceof Downloadable) {
                System.out.println("   Rejected: " + book.getTitle() + " cannot be borrowed, only downloaded");
                ((Downloadable) book).download();
            } else {
                System.out.println("   Rejected: " + book.getTitle() + " cannot be borrowed");
            }
            return;
        }

        Borrowable item = (Borrowable) book;
        if (item.isBorrowed()) {
            System.out.println("   Rejected: " + book.getTitle() + " is already borrowed");
            return;
        }

        item.borrow(reader.getName());
        reader.addBook(book);

        if (!borrowHistory.containsKey(book.getId())) {
            borrowHistory.put(book.getId(), new HashSet<>());
        }
        borrowHistory.get(book.getId()).add(reader);
    }

    public void returnBook(int readerId, int bookId) {
        Reader reader = findReaderById(readerId);
        Book book = findBookById(bookId);
        if (reader == null || book == null) {
            System.out.println("Error: reader or book not found");
            return;
        }
        if (!reader.getBorrowedBooks().contains(book)) {
            System.out.println("Error: " + reader.getName() + " does not have " + book.getTitle());
            return;
        }
        if (book instanceof Borrowable) {
            ((Borrowable) book).returnItem();
        }
        reader.removeBook(book);
    }

    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getAuthor().equals(author)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> findCheaperThan(double maxPrice) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getPrice() < maxPrice) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> list = booksByCategory.get(category);
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }

    public List<Book> getSortedBooks() {
        List<Book> copy = new ArrayList<>(books);
        Collections.sort(copy);
        return copy;
    }

    public List<Book> getSortedBooks(Comparator<Book> comparator) {
        List<Book> copy = new ArrayList<>(books);
        Collections.sort(copy, comparator);
        return copy;
    }

    public Set<String> getAllAuthors() {
        Set<String> authors = new TreeSet<>();
        for (Book b : books) {
            authors.add(b.getAuthor());
        }
        return authors;
    }

    public void printStatistics() {
        System.out.println("Total books: " + books.size());
        System.out.println("Total readers: " + readersById.size());
        System.out.println("Requests in queue: " + requests.size());

        if (books.isEmpty()) {
            System.out.println("Library is empty, no more statistics");
            return;
        }

        for (String category : booksByCategory.keySet()) {
            System.out.println("   " + category + ": " + booksByCategory.get(category).size() + " book(s)");
        }

        double sum = 0;
        Book mostExpensive = books.get(0);
        for (Book b : books) {
            sum += b.getPrice();
            if (b.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = b;
            }
        }
        System.out.println("Average price: $" + String.format("%.2f", sum / books.size()));
        System.out.println("Most expensive: " + mostExpensive + " ($" + mostExpensive.getPrice() + ")");

        int borrowedCount = 0;
        for (Book b : books) {
            if (b instanceof Borrowable && ((Borrowable) b).isBorrowed()) {
                borrowedCount++;
            }
        }
        System.out.println("Borrowed now: " + borrowedCount);
    }

    public void printBorrowHistory() {
        if (borrowHistory.isEmpty()) {
            System.out.println("History is empty");
            return;
        }
        for (Integer bookId : borrowHistory.keySet()) {
            System.out.println("   " + findBookById(bookId) + " was borrowed by " + borrowHistory.get(bookId));
        }
    }

    public void printReaders() {
        for (Reader r : readersById.values()) {
            System.out.println("   " + r + " has " + r.getBorrowedBooks());
        }
    }

    public static void printBooks(List<Book> list) {
        if (list.isEmpty()) {
            System.out.println("   (nothing found)");
            return;
        }
        for (Book b : list) {
            System.out.println("   " + b + " | " + b.getAuthor() + " | " + b.getYear() + " | $" + b.getPrice());
        }
    }
}
