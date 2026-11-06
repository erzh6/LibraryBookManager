package models;

import interfaces.Borrowable;
import interfaces.Discountable;

public class Textbook extends Book implements Borrowable, Discountable {
    private String subject;
    private int edition;
    private boolean borrowed;
    private String borrower;

    public Textbook(int id, String title, String author, int year, double price,
                    String subject, int edition) {
        super(id, title, author, year, price);
        this.subject = subject;
        setEdition(edition);
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getEdition() { return edition; }
    public void setEdition(int edition) {
        if (edition <= 0) {
            throw new IllegalArgumentException("edition must be positive");
        }
        this.edition = edition;
    }

    @Override
    public String getCategory() { return "Textbook"; }

    @Override
    public int getLoanPeriodDays() { return 14; }

    @Override
    public double calculateLateFee(int daysLate) { return daysLate * 0.50; }

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
    public double getDiscountPercent() { return isNewEdition() ? 0 : 20; }

    @Override
    public double getDiscountedPrice() {
        return getPrice() * (1 - getDiscountPercent() / 100);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("   Subject: " + subject + ", Edition: " + edition);
    }

    public boolean isNewEdition() { return edition >= 3; }
}
