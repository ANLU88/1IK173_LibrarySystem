import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class User {

    /** Value for "type"
        3 = Undergradute, max = 3
        5 = Postgraduate, max 5
        7 = PHD/candidate, max 7
        10 = Teacher/Professor max 10
     **/

    /** Value for "active"
     *  1 = active
     *  2 = not active
     */

    public User() {
    }

    private int id;
    private String fNamn;
    private String lNamn;
    int pId;
    private int type;
    private int itemsBorrowed;
    private int borrowLimit;
    private int active;                     // Boolen istället?
    int delays;                     // Number of days delayed?
    private Date suspendedDate;
    Book [] bookList = new Book [itemsBorrowed];



    public User(int id, String fNamn, String lNamn, int pId, int type, int itemsBorrowed, int borrowLimit, int active, int delays, Date suspendedDate) {
        this.id = id;
        this.fNamn = fNamn;
        this.lNamn = lNamn;
        this.pId = pId;
        this.type = type;
        this.itemsBorrowed = itemsBorrowed;
        this.borrowLimit = borrowLimit;
        this.active = active;
        this.delays = delays;
        this.suspendedDate = suspendedDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfNamn() {
        return fNamn;
    }

    public void setfNamn(String fNamn) {
        this.fNamn = fNamn;
    }

    public String getlNamn() {
        return lNamn;
    }

    public void setlNamn(String lNamn) {
        this.lNamn = lNamn;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getItemsBorrowed() {
        return itemsBorrowed;
    }

    public void setItemsBorrowed(int itemsBorrowed) {
        this.itemsBorrowed = itemsBorrowed;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDelays() {
        return delays;
    }

    public void setDelays(int delays) {
        this.delays = delays;
    }

    public Date getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(Date suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    public Book[] getBookList() {
        return bookList;
    }

    public void addBook (Book bookData) {
        if (borrowLimit > itemsBorrowed) {
            bookList[itemsBorrowed] = bookData;
        }
    }

}
