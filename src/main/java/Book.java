import java.util.Date;

public class Book {

    private int id;
    private int isbn;
    private String title;
    private Date dateBorrowed;

    public Book() {
    }

    public Book (int id, int isbn, String title, Date dateBorrowed)  {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.dateBorrowed = dateBorrowed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }
}
