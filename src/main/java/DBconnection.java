import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {

    private static Logger logger = LogManager.getLogger(DBconnection.class.getName());

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> userLista = new ArrayList<>();


        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM user");

            while(rs.next()){
                User anvandare = new User(
                        rs.getInt("id"),
                        rs.getString("fnamn"),
                        rs.getString("lnamn"),
                        rs.getInt("pid"),
                        rs.getInt("type"),
                        rs.getInt("itemsBorrowed"),
                        rs.getInt("borrowLimit"),
                        rs.getInt("active"),
                        rs.getInt("delays"),
                        rs.getDate("suspenddate")
                );
                userLista.add(anvandare);
            }
        }

        catch(SQLException ex){
            System.out.println("Something went wrong");
        }

        return userLista;
    }

    public ArrayList<Book> getBooks() throws SQLException {
        ArrayList<Book> bookList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM book");

            while (rs.next()){
                Book bok = new Book (
                        rs.getInt("bookID"),
                        rs.getInt("ISBN"),
                        rs.getString("bokTitle"),
                        rs.getDate("dateBorrowed")
                );
                bookList.add(bok);
            }
        }

        catch(SQLException ex) {
            System.out.println("Something went wrong");
        }

        return bookList;
    }

    public void deleteUser(int userId) throws SQLException {
        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            //PreparedStatement deleteUser = connection.prepareStatement("DELETE FROM user WHERE id = " + "'" + userId + "'");
            PreparedStatement deleteUser = connection.prepareStatement("DELETE FROM user WHERE id=?");
            deleteUser.setInt(1, userId);
            deleteUser.executeUpdate();
        }
        catch (SQLException exception) {
            System.out.println("Something went wrong");
        }
        System.out.println("Deletion of user succesful!");
    }

    public void addUser (int id, String fnamn, String lnamn, int pId, int type, int itemsBorrowed, int borrowLimit, int active, int delays, Date suspendedDate) throws SQLException {

        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            PreparedStatement userInsert = connection.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?,?,?,null,null)");
            userInsert.setInt(1, id);
            userInsert.setString(2, fnamn);
            userInsert.setString(3, lnamn);
            userInsert.setInt(4, pId);
            userInsert.setInt(5, type);
            userInsert.setInt(6, itemsBorrowed);
            userInsert.setInt(7, borrowLimit);
            userInsert.setInt(8, active);

            userInsert.executeUpdate();
            System.out.println(
                    "\nUser " + fnamn + " " + lnamn + " is now created \n" +
                    "Your userId is " + id + "\n" +
                    "You are allowed to borrow " + borrowLimit + " number of books"
            );

        }

        catch (SQLException exception) {
            System.out.println("Something went wrong " + exception.getMessage());
        }
    }

    public void addBook (int id, int isbn, String title) {
        logger.trace("--> addBook");


        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            PreparedStatement prepStat = conn.prepareStatement("INSERT INTO Book VALUES (?,?,?,null,null ) ");
                prepStat.setInt (1,id);
                prepStat.setInt (2,isbn);
                prepStat.setString(3, title);

                prepStat.executeUpdate();
            System.out.println("\nBoken har nu lagts till\n" +
                    "Titel:\t" + title + "\n" +
                    "ID:\t\t" + id + "\n" +
                    "ISBN:\t" + isbn);
        }

        catch(SQLException ex) {
            System.out.println("AddBook - Something went wrong" + ex.getMessage());
            logger.error("Something went wrong! No book was added!");
        }

        logger.trace("<---addBook");
    }

    public void addBookToUser (int userId, int bookId, int itemsBorrowed, Date datum) throws SQLException {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            // Uppdatera userID på bok
            PreparedStatement setUsertoBook = conn.prepareStatement("UPDATE book SET userID=? WHERE bookID=?");
            setUsertoBook.setInt(1, userId);
            setUsertoBook.setInt(2, bookId);
            setUsertoBook.executeUpdate();

            // Uppdatera antalet lånade böcker på användare
            PreparedStatement updateItemBorr = conn.prepareStatement("UPDATE user SET itemsBorrowed=? WHERE id=?");
            updateItemBorr.setInt(1, itemsBorrowed);
            updateItemBorr.setInt(2, userId);
            updateItemBorr.executeUpdate();

            // Uppdatera dateBorrowed till dagens datum
            PreparedStatement setBorrowedDate = conn.prepareStatement("UPDATE book SET dateBorrowed=? WHERE bookID=?");
            setBorrowedDate.setDate(1, datum);
            setBorrowedDate.setInt(2, bookId);
            setBorrowedDate.executeUpdate();

        }
        catch (SQLException ex) {
            System.out.println("addBookToUser - Something whent wrong: " + ex.getMessage());
        }
    }

    public void removeBookFromUser(int bookId, int userID) throws SQLException {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            PreparedStatement bookIn = conn.prepareStatement("UPDATE book SET userID = NULL WHERE bookID =?");
            bookIn.setInt(1, bookId);
            bookIn.executeUpdate();

            PreparedStatement inDateBook = conn.prepareStatement("UPDATE book SET dateBorrowed = NULL WHERE bookID=?");
            inDateBook.setInt(1, bookId);
            inDateBook.executeUpdate();

            int borrowedItems = getItemsborrowed(userID) - 1;

            PreparedStatement borrowedItemsIn = conn.prepareStatement("UPDATE user SET itemsBorrowed=? WHERE id=?");
            borrowedItemsIn.setInt(1, borrowedItems);
            borrowedItemsIn.setInt(2, userID);
            borrowedItemsIn.executeUpdate();
        }

        catch (SQLException ex) {
            System.out.println("removeBookFromUser - Something went wrong " + ex.getMessage());
        }
    }

    public int getItemsborrowed (int userId){
        int itemsBorrowed = 0;

        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

/*
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT user.itemsBorrowed from user WHERE user.Id = " + "'" + userId + "'");

            String query = ("SELECT itemsBorrowed FROM user WHERE id =  + userId");

         ResultSet rs = statement.executeQuery(query);

          while (rs.next()){
                itemsBorrowed = rs.getInt("ItemBorrowed");
            }
*/

            PreparedStatement getItem = conn.prepareStatement("SELECT itemsBorrowed FROM user WHERE id=?");
            getItem.setInt(1, userId);
            getItem.executeUpdate();


        }

        catch (SQLException ex) {
            System.out.println("getItemsborrowed - Something went wrong" + ex.getMessage());
        }
        return itemsBorrowed;
    }

    public void deleteBook(int id) {

        logger.trace("----> deleteBok");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            PreparedStatement deleteBok = conn.prepareStatement("DELETE FROM book WHERE bookID = ?");
            deleteBok.setInt(1, id);
            deleteBok.executeUpdate();

            System.out.println("\nBook is now deleted from the Library System!");
            logger.trace("<---- deleteBok");

        }
        catch (SQLException ex) {
            System.out.println("Something went wrong");
            logger.error("Something went wrong! No book got deleted!");
        }
    }

    public Book[] getBooksBorrowedByUser (int userID) throws SQLException {
        Book [] booksLendedByUser = new Book[10];
        int counter = 0;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM book WHERE book.id = " + "'" + userID + "'");

            while (rs.next()) {
                Book book = new Book (rs.getInt("id"), rs.getInt("ISBN"), rs.getString("bokTitle"), rs.getDate("dateBorrowed"));
                booksLendedByUser[counter] = book;
                counter++;
            }
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong" + ex.getMessage());
        }

        return booksLendedByUser;
    }

    public void resetSuspend(int userId) throws SQLException {

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/schema_name",
                    "1IK173_Lib",
                    "1IK173@LinneUniversitet")) {

                //PreparedStatement bookIn = conn.prepareStatement("UPDATE user SET suspendDate = null WHERE user.id =" + "'" + userId + "'");
                PreparedStatement bookIn = conn.prepareStatement("UPDATE user SET suspendDate = ? WHERE id = ?");
                bookIn.setDate(1, null);
                bookIn.setInt(2, userId);
                bookIn.executeUpdate();
            }

            catch (SQLException ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
    }

    public void suspendUser (int userId, Date suspendDate) throws SQLException {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {

            PreparedStatement setSusDate = conn.prepareStatement("UPDATE user SET suspenddate = ? WHERE id = ?");
            setSusDate.setDate(1, suspendDate);
            setSusDate.setInt(2, userId);
            setSusDate.executeUpdate();
        }
         catch (SQLException ex) {
             System.out.println("Something when wrong: " + ex.getMessage());
         }
    }

    public void setDelays(int userId, int delays) throws SQLException{
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schema_name",
                "1IK173_Lib",
                "1IK173@LinneUniversitet")) {


            //PreparedStatement bookIn = conn.prepareStatement("UPDATE user SET delays = " + "'" + delays + "'" + "WHERE user.id = " + "'");
            PreparedStatement bookIn = conn.prepareStatement("UPDATE user SET delays = ? WHERE id = ?");
            bookIn.setInt(1, delays);
            bookIn.setInt(2, userId);
            bookIn.executeUpdate();
        }

        catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
   }

    // Not int use
    public void updateBorrowedDate (Date dateBorrowed, int bookID) throws SQLException {
       try (Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/schema_name",
               "1IK173_Lib",
               "1IK173@LinneUniversitet")) {

           PreparedStatement updateDateBorrowed = conn.prepareStatement("UPDATE book SET dateBorrwed = ? WHERE bookID = ?");
           updateDateBorrowed.setDate(1, dateBorrowed);
           updateDateBorrowed.setInt(2, bookID);
           updateDateBorrowed.executeUpdate();

       }

       catch (SQLException ex) {
           System.out.println("Something went wrong: " + ex.getMessage());
       }

   }

    public static void main(String[] args) throws SQLException{

        }
}