import comparators.PriceComparator;
import exceptions.BookNotFoundException;
import comparators.TitleComparator;
import comparators.YearThenTitleComparator;
import models.Book;
import models.EBook;
import models.Novel;
import models.Textbook;
import roles.Librarian;
import roles.Reader;
import services.Library;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library();
    private static Librarian librarian = new Librarian(1, "Mrs. Smith");

    public static void main(String[] args) {
        loadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            System.out.println();

            switch (choice) {
                case 1: showAllBooks(); break;
                case 2: addBook(); break;
                case 3: updateBook(); break;
                case 4: removeBook(); break;
                case 5: findBook(); break;
                case 6: filterBooks(); break;
                case 7: sortBooks(); break;
                case 8: addReader(); break;
                case 9: library.printReaders(); break;
                case 10: submitRequest(); break;
                case 11: returnBook(); break;
                case 12: library.processNextRequest(); break;
                case 13: librarian.processAllRequests(library); break;
                case 14: library.printBorrowHistory(); break;
                case 15: library.printStatistics(); break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Wrong choice, try again");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("LIBRARY");
        System.out.println("\n--- Books ---");
        System.out.println("1. Show all books");
        System.out.println("2. Add book");
        System.out.println("3. Update book");
        System.out.println("4. Remove book");
        System.out.println("5. Find book by id");
        System.out.println("6. Filter books");
        System.out.println("7. Sort books");
        System.out.println("\n--- Reader ---");
        System.out.println("8. Add reader");
        System.out.println("9. Show readers");
        System.out.println("10. Submit borrow request");
        System.out.println("11. Return book");
        System.out.println("\n--- Librarian ---");
        System.out.println("12. Process next request");
        System.out.println("13. Process all requests");
        System.out.println("\n--- Reports ---");
        System.out.println("14. Borrow history");
        System.out.println("15. Statistics");
        System.out.println("0. Exit");
    }

    private static void loadSampleData() {
        library.addBook(new Textbook(1, "Math", "Salamov Amir", 2018, 40.0, "Math", 8));
        library.addBook(new Textbook(2, "Physics", "Salamov Amir", 2005, 35.0, "Physics", 2));
        library.addBook(new EBook(3, "Game of Thrones", "Aigerim Sh.", 2008, 15.0, "PDF", 12.5));
        library.addBook(new EBook(4, "Fight Club", "Davlet F.", 2020, 12.0, "EPUB", 80.0));
        library.addBook(new Novel(5, "Dexter", "Erzhan Dokturbaev", 2015, 18.0, "Thriller", 620));
        library.addBook(new Novel(6, "History", "Diana Kim", 2015, 10.0, "Drama", 300));
        library.addReader(new Reader(1, "Alice"));
        library.addReader(new Reader(2, "Bob"));
        library.addReader(new Reader(3, "Charlie"));
    }

    private static void showAllBooks() {
        Library.printBooks(library.getSortedBooks());
    }

    private static void addBook() {
        System.out.println("Type: 1 - Textbook, 2 - EBook, 3 - Novel");
        int type = readInt("Choose type: ");
        if (type < 1 || type > 3) {
            System.out.println("Wrong type");
            return;
        }

        int id = readInt("Id: ");
        String title = readText("Title: ");
        String author = readText("Author: ");
        int year = readInt("Year: ");
        double price = readDouble("Price: ");

        try {
            Book book;
            if (type == 1) {
                String subject = readText("Subject: ");
                int edition = readInt("Edition: ");
                book = new Textbook(id, title, author, year, price, subject, edition);
            } else if (type == 2) {
                String format = readText("Format (PDF/EPUB): ");
                double fileSize = readDouble("File size (MB): ");
                book = new EBook(id, title, author, year, price, format, fileSize);
            } else {
                String genre = readText("Genre: ");
                int pages = readInt("Pages: ");
                book = new Novel(id, title, author, year, price, genre, pages);
            }
            library.addBook(book);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateBook() {
        int id = readInt("Book id: ");
        String title = readText("New title: ");
        double price = readDouble("New price: ");
        library.updateBook(id, title, price);
    }

    private static void removeBook() {
        int id = readInt("Book id: ");
        try {
            library.removeBook(id);
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void findBook() {
        int id = readInt("Book id: ");
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("Book with id " + id + " not found");
        } else {
            book.displayInfo();
        }
    }

    private static void filterBooks() {
        System.out.println("1 - by author, 2 - cheaper than, 3 - by category");
        int type = readInt("Choose: ");
        if (type == 1) {
            String author = readText("Author: ");
            Library.printBooks(library.findByAuthor(author));
        } else if (type == 2) {
            double maxPrice = readDouble("Max price: ");
            Library.printBooks(library.findCheaperThan(maxPrice));
        } else if (type == 3) {
            String category = readText("Category (Textbook/EBook/Novel): ");
            Library.printBooks(library.getBooksByCategory(category));
        } else {
            System.out.println("Wrong choice");
        }
    }

    private static void sortBooks() {
        System.out.println("1 - by id, 2 - by title, 3 - by price, 4 - by year then title");
        int type = readInt("Choose: ");
        if (type == 1) {
            Library.printBooks(library.getSortedBooks());
        } else if (type == 2) {
            Library.printBooks(library.getSortedBooks(new TitleComparator()));
        } else if (type == 3) {
            Library.printBooks(library.getSortedBooks(new PriceComparator()));
        } else if (type == 4) {
            Library.printBooks(library.getSortedBooks(new YearThenTitleComparator()));
        } else {
            System.out.println("Wrong choice");
        }
    }

    private static void addReader() {
        int id = readInt("Reader id: ");
        String name = readText("Name: ");
        try {
            library.addReader(new Reader(id, name));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void submitRequest() {
        int readerId = readInt("Reader id: ");
        int bookId = readInt("Book id: ");
        library.submitRequest(readerId, bookId);
    }

    private static void returnBook() {
        int readerId = readInt("Reader id: ");
        int bookId = readInt("Book id: ");
        library.returnBook(readerId, bookId);
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine();
            try {
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
