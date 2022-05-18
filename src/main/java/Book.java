public class Book {

    private int id;
    private int isbn;
    private String title;
    private int noOfCopys;

    public void Book () {
    }

    public void Book (int id, int isbn, String title, int noOfCopys) {
        this.id = id;
        this. isbn = isbn;
        this.title = title;
        this. noOfCopys = noOfCopys;
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

    public int getNoOfCopys() {
        return noOfCopys;
    }

    public void setNoOfCopys(int noOfCopys) {
        this.noOfCopys = noOfCopys;
    }
}
