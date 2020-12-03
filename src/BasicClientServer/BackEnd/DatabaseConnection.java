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
import java.util.ArrayList;

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
            System.out.println("Database failed to connect");
        }
    }

    public void processLogin(String username,String password,NetworkAccess na, ClientHandler ch) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT *, count(*) FROM SWEGroup3DB.users WHERE username = '" + username + "'");
        if (resultSet.next()) {
            if (resultSet.getString(5).equals("0")) { //if username is not found count(*) would be 0
                na.sendString("Invalid", false);
                return;
            }
            int lockCount = Integer.parseInt(resultSet.getString(4));
            if (resultSet.getString(3).equals(password) && lockCount != 3) {
                ch.getServer().addLoggedInClient(ch);
                na.sendString("Success", false);
                    try {
                        stmt.executeUpdate("UPDATE SWEGroup3DB.users SET lockcount = 0 WHERE username = '" + username + "'"); //Resetting Lock count to 0
                    } catch(SQLException e) {
                        System.out.println("Caught Exception after Resetting Lock Count after Successful Login");
                    }
                return;
            } else {
                //increment lockcount
                if (lockCount == 3) {
                    na.sendString("Locked", false);
                    return;
                }
                lockCount++;
                try {
                    stmt.executeUpdate("UPDATE SWEGroup3DB.users SET lockcount = '" + lockCount + "' WHERE username = '" + username + "'"); //Updates users lockCount in db
                } catch(SQLException e) {
                    System.out.println("Login SQL Exception");
                }
                na.sendString("Invalid", false);
            }
        }
    }

    public String DBQueryRegisteredUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select count(*) from SWEGroup3DB.users");
        while (resultSet.next()) {
            return (resultSet.getString(1));
        }
        return null;
    }

    public String DBQuerySignedUpUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from SWEGroup3DB.users");
        String result="";
        while (resultSet.next()) {
            result += (resultSet.getString(1)) + ",";
        }
        return result;
    }

    public String DBQueryLockedOutUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT username, count(*) from SWEGroup3DB.users where lockcount=3");
        String result="";
        while (resultSet.next()) {
            if(resultSet.getString(2).equals("0")) return "0";
            result += (resultSet.getString(1));
            result += ",";
        }
        return result;
    }

    public void processPasswordRecovery(String username, NetworkAccess na, ClientHandler ch) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT email, password, count(*) FROM SWEGroup3DB.users WHERE username = '" + username + "'");
        if(resultSet.next()) {
            if(resultSet.getString(3).equals("1")) {
                SendEmailGmailSMTP.sendMail(resultSet.getString(1), resultSet.getString(2)); //Sending password to Email
                try {
                    stmt.executeUpdate("UPDATE SWEGroup3DB.users SET lockcount = 0 WHERE username = '" + username + "'"); //Resetting Lock count to 0
                } catch(SQLException e) {
                    System.out.println("Caught Exception after update");
                }
                na.sendString("Success", false);
            } else if(resultSet.getString(3).equals("0")) {
                na.sendString("Failed", false);
            }
        }

    }

    public void processAccountCreation(String email, String username, String password, NetworkAccess na, ClientHandler ch) throws SQLException {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO SWEGroup3DB.users (username, email, password, lockcount) VALUES ('" + username + "', '" + email + "', '" + password + "', 0)");
            na.sendString("Success", false);
        } catch(SQLException e) {
            na.sendString("Failed", false); //User already exists
        }
    }

    public void processChangePassword(String username, String oldPassword, String newPassword, NetworkAccess na, ClientHandler ch) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT username, password, count(*) FROM SWEGroup3DB.users WHERE username = '" + username + "'");

        if(resultSet.next()) {
            if(resultSet.getString((3)).equals("0")) {
                na.sendString("WrongUsername", false);
                return;
            }
            if(resultSet.getString(2).equals(oldPassword)) {
                try {
                    stmt.execute("UPDATE SWEGroup3DB.users SET password = '" + newPassword + "' WHERE username = '" + username + "'");
                } catch(SQLException e) {
                    System.out.println("Change Password SQL Execute Error");
                }
                na.sendString("Success", false);
            } else {
                na.sendString("WrongOldPassword", false);
            }

        }
    }

    public ArrayList<Vehicle> getVehicleData() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM SWEGroup3DB.vehicles");
        ArrayList<Vehicle> vehicles=new ArrayList<>();
        while (resultSet.next()) {
            Vehicle vehicle = new Vehicle(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), Integer.parseInt(resultSet.getString(5)), resultSet.getString(6));
            vehicles.add(vehicle);
        }
        return vehicles;
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