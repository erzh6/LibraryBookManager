package models;

import interfaces.Discountable;
import interfaces.Downloadable;

public class EBook extends Book implements Downloadable, Discountable {
    private String format;
    private double fileSize;

    public EBook(int id, String title, String author, int year, double price,
                 String format, double fileSize) {
        super(id, title, author, year, price);
        this.format = format;
        setFileSize(fileSize);
    }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public void setFileSize(double fileSize) {
        if (fileSize <= 0) {
            throw new IllegalArgumentException("file size must be positive");
        }
        this.fileSize = fileSize;
    }

    @Override
    public String getCategory() { return "EBook"; }

    @Override
    public int getLoanPeriodDays() { return 21; }

    @Override
    public double calculateLateFee(int daysLate) { return 0; }

    @Override
    public void download() {
        System.out.println("   Downloading " + getTitle() + " (" + fileSize + " MB, " + format + ")");
    }

    @Override
    public double getFileSize() { return fileSize; }

    @Override
    public boolean isLarge() { return fileSize > 50; }

    @Override
    public double getDiscountPercent() { return 30; }

    @Override
    public double getDiscountedPrice() {
        return getPrice() * (1 - getDiscountPercent() / 100);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("   Format: " + format + ", File size: " + fileSize + " MB");
    }
}
