package models;

import interfaces.Borrowable;

public class Novel extends Book implements Borrowable {
    private String genre;
    private int pages;
    private boolean borrowed;
    private String borrower;

    public Novel(int id, String title, String author, int year, double price,
                 String genre, int pages) {
        super(id, title, author, year, price);
        this.genre = genre;
        setPages(pages);
    }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getPages() { return pages; }
    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("pages must be positive");
        }
        this.pages = pages;
    }

    @Override
    public String getCategory() { return "Novel"; }

    @Override
    public int getLoanPeriodDays() { return pages > 500 ? 30 : 21; }

    @Override
    public double calculateLateFee(int daysLate) { return daysLate * 0.25; }

    @Override
    public void borrow(String readerName) {
        borrowed = true;
        borrower = readerName;
        System.out.println("   " + readerName + " borrowed " + getTitle()
                + " for " + getLoanPeriodDays() + " days");
    }

    @Override
    public void returnItem() {
        System.out.println("   " + borrower + " returned " + getTitle());
        borrowed = false;
        borrower = null;
    }

    @Override
    public boolean isBorrowed() { return borrowed; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("   Genre: " + genre + ", Pages: " + pages);
    }
}
