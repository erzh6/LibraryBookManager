package interfaces;

public interface Borrowable {
    void borrow(String readerName);
    void returnItem();
    boolean isBorrowed();
}
