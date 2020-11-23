package BasicClientServer.BackEnd;

// -- download MySQL from: http://dev.mysql.com/downloads/
//    Community Server version
// -- Installation instructions are here: http://dev.mysql.com/doc/refman/5.7/en/installing.html
// -- open MySQL Workbench to see the contents of the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

// -- MAKE SURE THE JDBC CONNECTOR JAR IS IN THE BUILD PATH
//    workspace -> properties -> Java Build Path -> Libraries -> Add External JARs...

// https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html for example SQL statements
public class DatabaseConnection {

    // -- objects to be used for database access
    private Connection conn = null;

    // -- root/admin
    // -- connect to the world database
    // -- this is the connector to the database, default port is 3306
    //    <<Your schema name here>> is the schema (database) you created using the workbench
    private String userdatabaseURL = "jdbc:mysql://swegroup3db.cvjkmq6byqae.us-west-1.rds.amazonaws.com:3306/?user=root";

    // -- this is the username/password, created during installation and in MySQL Workbench
    //    When you add a user make sure you give them the appropriate Administrative Roles
    //    (DBA sets all which works fine)
    private String user = "root";
    private String password = "Group32020!";

    public DatabaseConnection() {
        String sqlcmd;

        try {
            // -- make the connection to the database
            //    performs functionality of SQL: use <<your schema>>;
            conn = DriverManager.getConnection(userdatabaseURL, user, password);

        }
        catch(Exception e){
            System.out.println("no workie");
        }
    }

    public void processLogin(String username,String password,NetworkAccess na, ClientHandler ch) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT *, count(*) FROM SWEGroup3DB.users WHERE username = '" + username + "'");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            //if the username is invalid
            //the query did not pull any results for usernames of that string
            if (resultSet.getString(5).equals("0")) {
                na.sendString("invalid", true);
                return;
            }
            if (resultSet.getString(2).equals(password)) {
                na.sendString("success", true);
                ch.getServer().addLoggedInClient(ch);
                return;
            } else {
                //increment lockcount
                int lockCount = Integer.parseInt(resultSet.getString(4).toString());
                if (lockCount == 3) {
                    na.sendString("locked", true);
                    return;
                }
                lockCount++;
                //need to update the lockCount in the database
//                stmt.executeUpdate("UPDATE SWE.Group3DB.users SET lockcount = '" + lockCount + "' WHERE username = '" + username + "'"); fix this
//                You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '.users SET lockcount = '1' WHERE username = 'keith'' at line 1
                na.sendString("invalid", true);
            }
        }
    }

    public String DBQueryRegisteredUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select count(*) from SWEGroup3DB.users");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            return (resultSet.getString(1));
        }
        return null;
    }

    public String DBQuerySignedUpUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from SWEGroup3DB.users");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        String result="";
        while (resultSet.next()) {
            result += (resultSet.getString(1)) + ",";
        }
        return result;
    }

    public String DBQueryLockedOutUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT username, count(*) from SWEGroup3DB.users where lockcount=3");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        String result="";
        while (resultSet.next()) {
            result += (resultSet.getString(1));
            result += ",";
        }
        return result;
    }

    //            if (rset.next()) {
//                System.out.println("MySQL Version: " + rset.getString(1));
//            }
//
//
//            /*
//             * EVERYTHING THAT FOLLOWS IS DEPENDENT ON THE TABLES AND COLUMNS
//             * THAT YOU CREATED WITHIN YOUR SCHEMA. YOU MUST MODIFY THIS CODE
//             * TO MATCH THE DATABASE CONFIGURATION. THESE ARE ONLY EXAMPLES.
//             */
//            // -- a query will return a ResultSet
//            sqlcmd = "SELECT * FROM users;";
//            rset = stmt.executeQuery(sqlcmd);
//
//            // -- the metadata tells us how many columns in the data
//            System.out.println("Table Columns:");
//            ResultSetMetaData rsmd = rset.getMetaData();
//            int numberOfColumns = rsmd.getColumnCount();
//            for (int i = 1; i <= numberOfColumns; ++i) {
//                System.out.print(rsmd.getColumnLabel(i) + "\t");
//            }
//            System.out.println();
//
//            // -- loop through the ResultSet one row at a time
//            //    Note that the ResultSet starts at index 1
//            while (rset.next()) {
//                // -- loop through the columns of the ResultSet
//                for (int i = 1; i < numberOfColumns; ++i) {
//                    System.out.print(rset.getString(i) + "\t\t");
//                }
//                System.out.println();
//            }
//
//            // -- select a specific username
//            System.out.println("getting user data for ccrdoc");
//            String selname = "ccrdoc";
//            //        SELECT * FROM users WHERE username = 'ccrdoc'
//            sqlcmd = "SELECT * FROM users WHERE username = '" + selname + "'";
//            rset = stmt.executeQuery(sqlcmd);
//            // -- loop through the ResultSet one row at a time
//            //    Note that the ResultSet starts at index 1
//            while (rset.next()) {
//                // -- loop through the columns of the ResultSet
//                for (int i = 1; i <= numberOfColumns; ++i) {
//                    System.out.print(rset.getString(i) + "\t\t");
//                }
//                System.out.println();
//            }
//
//
//            // -- test an insert statement. Note that this will throw an exception if
//            //    the key already exists in the database
//            System.out.println("inserting into the database");
//            String uname = "ccrdoc2";
//            String pword = "ccrdoc1234";
//            String email = "ccrdoc@gmail.com";
//            try {
//                //        INSERT INTO users VALUES('ccrdoc2', 'ccrdoc1234', 'ccrdoc@gmail.com')
//                sqlcmd = "INSERT INTO users VALUES('" + uname + "', '" + pword + "', '" + email + "')";
//                stmt.executeUpdate(sqlcmd);
//            }
//            catch (SQLException ex) {
//                System.out.println("SQLException: " + ex.getMessage());
//            }
//            // UPDATE `csc335`.`users` SET `password` = '1234ccrdoc' WHERE (`username` = 'ccrdoc2');
//            try {
//                //        INSERT INTO users VALUES('ccrdoc2', 'ccrdoc1234', 'ccrdoc@gmail.com')
//                sqlcmd = "UPDATE users SET password = '1234ccrdoc' WHERE (username = 'ccrdoc2')";
//                stmt.executeUpdate(sqlcmd);
//            }
//            catch (SQLException ex) {
//                System.out.println("SQLException: " + ex.getMessage());
//            }
//
//            System.out.println("selecting all records from data base");
//            sqlcmd = "SELECT * FROM users;";
//            rset = stmt.executeQuery(sqlcmd);
//            // -- loop through the ResultSet one row at a time
//            //    Note that the ResultSet starts at index 1
//            while (rset.next()) {
//                // -- loop through the columns of the ResultSet
//                for (int i = 1; i < numberOfColumns; ++i) {
//                    System.out.print(rset.getString(i) + "\t\t");
//                }
//                System.out.println(rset.getString(numberOfColumns));
//            }
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());


    /**
     * @param args
     */
    public static void main(String[] args) {

        DatabaseConnection dbc = new DatabaseConnection();

    }

}