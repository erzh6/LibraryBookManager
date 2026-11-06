package comparators;

import models.Book;

import java.util.Comparator;

public class YearThenTitleComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        int result = Integer.compare(b2.getYear(), b1.getYear());
        if (result == 0) {
            result = b1.getTitle().compareTo(b2.getTitle());
        }
        return result;
    }
}
