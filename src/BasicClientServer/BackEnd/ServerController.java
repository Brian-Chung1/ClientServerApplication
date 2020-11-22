package BasicClientServer.BackEnd;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class ServerController extends Application {

    private static Server Server;

    public void start(Stage primaryStage) throws Exception {
        Parent ServerGUI = FXMLLoader.load(getClass().getResource("/ServerGUI.fxml")); //Server GUI
        Scene scene = new Scene(ServerGUI);
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    @FXML private TextArea ServerGUIDisplay;


    //This is how we add text without replacing the previous text
    //ServerGUIDisplay.appendText("some text here" + "\n"); The "\n" will add a new line


    public void QueryConnectedUsers(MouseEvent mouseEvent) {
        int connectedUsers = Server.getConnections();
        ServerGUIDisplay.setText("" + connectedUsers);
    }

    public void QueryLoggedInUsers(MouseEvent mouseEvent) {
        int connectedUsers = Server.getLoggedIn();
        ServerGUIDisplay.setText("" + connectedUsers);
    }

    public void QuerySignedUpUsers(MouseEvent mouseEvent) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        String result = db.DBQuerySignedUpUsers();
        ServerGUIDisplay.appendText(result + "\n");
    }

    public void QueryRegisteredUsers(MouseEvent mouseEvent) {

    }

    public void QueryLockedOutUsers(MouseEvent mouseEvent) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        String result = db.DBQueryLockedOutUsers();
        String[] users = result.split(",");

        for(String user : users) {
            ServerGUIDisplay.appendText(user + "\n");
        }
    }

    public void StartServer(MouseEvent mouseEvent) {
        Server = new Server();
        Thread serverthread = new Thread(Server);
        serverthread.start();
    }
}
