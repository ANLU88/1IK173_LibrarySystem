import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Support extends DBconnection {

    private static Logger logger = LogManager.getLogger(Support.class.getName());

    User anvandare;
    Book bok;
    DBconnection object;

    public Support(DBconnection obj) {
        object = obj;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> userLista = new ArrayList<>();

        try {
            userLista = object.getUsers();

        } catch (SQLException e) {
            System.out.println("Något gick fel med databas förbindelsen");
        }
        return userLista;
    }

    public ArrayList<Book> getBooks() {
        ArrayList<Book> bookLista = new ArrayList<>();

        try {
            bookLista = object.getBooks();
        }
        catch (SQLException e) {
            System.out.println("Något gick fel med databas förbindelsen");
        }
        return bookLista;
    }

    public User getAUser(int UserId) {
        anvandare = new User();

        for (User u : getUsers()) {
            if (u.getId() == UserId) {
                anvandare = u;
            }
        }
        return anvandare;
    }

    public void addBookToAUser(int userId, int bookId) {

        for (Book p : getBooks()) {

            if (p.getId() == bookId) {

                getAUser(userId).setItemsBorrowed(getAUser(userId).getItemsBorrowed() + 1);

                try {
                    object.addBookToUser(userId, bookId, getAUser(userId).getItemsBorrowed() + 1, java.sql.Date.valueOf(LocalDate.now()));
                }
                catch (SQLException e) {
                    System.out.println("Something went wrong with the connection to the database!");

                }
            }
        }
    }

    public void deleteUser(int userId) {
        logger.trace("---> deleteUser");

        int nummer = getAUser(userId).getId();
        User user = getAUser(userId);

        try {
            object.deleteUser(userId);
        }
        catch (SQLException e) {
            System.out.println("Something went wrong with the connection to the database");
            logger.error("Something went wring! Deletion of user not completed!");
        }

        if (nummer == userId) {
            int index = getUsers().indexOf(user);
            getUsers().remove(index);
        }
        else {
            System.out.println("User does not exist!");
            logger.error("User does not exist and can't be deleted!");
        }
        logger.trace("<--- deleteUser");
    }

    public User getAUserOnPersonId(int personId) {
        anvandare = new User();

        for (User s : getUsers()) {
            if (s.getpId() == personId) {
                anvandare = s;
            }
        }
        return anvandare;
    }

    public ArrayList<Book> getAllBooksOnISBN(int bookISBN) {
        bok = new Book();
        ArrayList<Book> books = new ArrayList<>();

        for (Book s : getBooks()) {
            if (s.getIsbn() == bookISBN) {
                books.add(s);
            }
        }
        return books;
    }

    public int getISBNOnTitle(String bookTitle) {
        logger.trace("---> getISBNOnTitle");

        bok = new Book();
        for (Book s : getBooks()) {
            if (s.getTitle().equals(bookTitle)) {
                bok = s;
            }
        }
        logger.trace("<--- getISBNOnTitle");
        return bok.getIsbn();
    }

    public int generateUserId() {
        Random random = new Random();
        int randomUserID = random.nextInt(8999) + 1000;

        for (User u : getUsers()) {
            if (u.getId() == randomUserID) {
                generateUserId();
            }
        }
        return randomUserID;
    }

    public int requestBook(String title, int userId) {
        logger.trace("---> requestBook");

        AdminInteraktionen AI = new AdminInteraktionen(object);

        Boolean checkIfSuspended = AI.CheckUserSuspended(userId);
        int ISBN = 0;
        int userType;
        userType = getAUser(userId).getType();
        User anvandare = getAUser(userId);

        if (checkIfSuspended.equals(false)) {

            switch (userType) {
                case 3:
                    if (userType == 3) {
                        if (anvandare.getItemsBorrowed() == 3) {
                            System.out.print("Undergraduate has reached the maximal amount of borrowed books and can't borrow more than three books! ");
                            break;
                        } else {
                            ISBN = getISBNOnTitle(title);

                            System.out.println("\nUser is a Undergraduate \n" +
                                    "Borrowlimit: " + anvandare.getBorrowLimit() + "\n" +
                                    "Borrowed right now: " + anvandare.getItemsBorrowed() +
                                    "\n\nUser with ID: " + userId + " can borrow book with ISBN: " + ISBN);

                            logger.info("User with ID: " + userId + " can borrow book with ISBN: " + ISBN);
                            break;
                        }
                    }
                case 5:
                    if (userType == 5) {
                        if (anvandare.getItemsBorrowed() >= 5) {
                            System.out.print("Postgraduate has reached the maximal amount of borrowed books and can't borrow more than five books! ");
                            break;
                        } else {
                            ISBN = getISBNOnTitle(title);

                            System.out.println("User is a Postgraduate \n" +
                                    "Borrowlimit: " + anvandare.getBorrowLimit() + "\n" +
                                    "Borrowed right now: " + anvandare.getItemsBorrowed() +
                                    "\n\nUser with ID: " + userId + " can borrow book with ISBN: " + ISBN);

                            logger.info("User with ID: " + userId + " can borrow book with ISBN: " + ISBN);
                            break;
                        }
                    }
                case 7:
                    if (userType == 7) {
                        if (anvandare.getItemsBorrowed() >= 7) {
                            System.out.println("Phd/Candidate has reached the maximal amount of borrowed books and can't borrow more than seven books! ");
                            break;
                        }
                        else {
                            ISBN = getISBNOnTitle(title);

                            System.out.println("\nUser is a Phd/Candidate \n" +
                                    "Borrowlimit: " + anvandare.getBorrowLimit() + "\n" +
                                    "Borrowed right now: " + anvandare.getItemsBorrowed() +
                                    "\n\nUser with ID: " + userId + " can borrow book with ISBN: " + ISBN);


                            logger.info("User with ID: " + userId + " can borrow book with ISBN: " + ISBN);
                            break;
                        }
                    }
                case 10:
                    if (userType == 10) {
                        if (anvandare.getItemsBorrowed() >= 10) {
                            System.out.print("Teacher/professor has reached the maximal amount of borrowed books and can't borrow more than ten books! ");
                            break;
                        }
                        else {
                            ISBN = getISBNOnTitle(title);

                            System.out.println("\nUser is a Teacher/professor \n" +
                                    "Borrowlimit: " + anvandare.getBorrowLimit() + "\n" +
                                    "Borrowed right now: " + anvandare.getItemsBorrowed() +
                                    "\n\nUser with ID: " + userId + " can borrow book with ISBN: " + ISBN);

                            logger.info("User with ID: " + userId + " can borrow book with ISBN: " + ISBN);
                            break;
                        }
                    }
            }
        }

        logger.trace("<--- requestBook");
        return ISBN;
    }

    public ArrayList<Book> getDelayedBooks(){
        ArrayList<Book> delayedBooks = new ArrayList<>();

        java.util.Date dagensDatum = java.sql.Date.valueOf(LocalDate.now());

        for(Book s: getBooks()){

            if(s.getDateBorrowed() != null){
                Calendar c = Calendar.getInstance();
                c.setTime(s.getDateBorrowed());
                c.add(Calendar.DATE, 15);

                if(c.getTime().compareTo(dagensDatum) < 0){
                    delayedBooks.add(s);
                }
            }
        }
        return delayedBooks;
    }

    public boolean updateDelays (int UserId){
        logger.trace("---> updateDelays");

        Support support = new Support(object);

        int amountOfdelays;
        amountOfdelays = getAUser(UserId).delays + 1;

        try {
            support.setDelays(UserId, amountOfdelays);
            System.out.println("Uppdatering av delays genomfördes hos användaren!");
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong with the connection to the databaase!");
            logger.error("Something went wrong! The users' delays were not updated!");
        }

        logger.trace("<--- updateDelays");
        return true;
    }

    public static void main(String[] args) {
        DBconnection connection = new DBconnection();
        Support support = new Support (connection);

        for(Book b: support.getDelayedBooks()){
            System.out.println(b.getId());
        }

    }
}