package BasicClientServer.BackEnd;

import BasicClientServer.FrontEnd.ClientGUIController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private static Thread ServerThread;
    boolean ServerRunning;

    public void start(Stage primaryStage) throws Exception {
        Parent ServerGUI = FXMLLoader.load(getClass().getResource("/ServerGUI.fxml")); //Server GUI
        Scene scene = new Scene(ServerGUI);
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Terminate Class");
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }


    @FXML private TextArea ServerGUIDisplay;
    @FXML private Button StartServerButton;


    //This is how we add text without replacing the previous text
    //ServerGUIDisplay.appendText("some text here" + "\n"); The "\n" will add a new line


    public void QueryConnectedUsers(MouseEvent mouseEvent) { //Number of Connected Users
        if(!ServerRunning) {
            alertServer("Server is not running", "");
            return;
        }
        clearDisplay(ServerGUIDisplay);
        int connectedUsers = Server.getConnections();
        ServerGUIDisplay.setText("Connected Users: " + connectedUsers);
    }

    public void QueryLoggedInUsers(MouseEvent mouseEvent) { //Number of Logged In Users
        if(!ServerRunning) {
            alertServer("Server is not running", "");
            return;
        }
        clearDisplay(ServerGUIDisplay);
        int connectedUsers = Server.getLoggedIn();
        ServerGUIDisplay.setText("Logged In Users: " + connectedUsers);
    }

    public void QuerySignedUpUsers(MouseEvent mouseEvent) throws SQLException { //Show List of Signed Up Users
        if(!ServerRunning) {
            alertServer("Server is not running", "");
            return;
        }
        clearDisplay(ServerGUIDisplay);
        String result = Server.getDB().DBQuerySignedUpUsers();
        String[] users = result.split(",");
        ServerGUIDisplay.appendText("Signed Up Users" + "\n");
        for(String user : users) {
            ServerGUIDisplay.appendText(user + "\n");
        }
    }

    public void QueryRegisteredUsers(MouseEvent mouseEvent) throws SQLException { //Number of Signed Up Users
        if(!ServerRunning) {
            alertServer("Server is not running", "");
            return;
        }
        clearDisplay(ServerGUIDisplay);
        String result = Server.getDB().DBQueryRegisteredUsers();
        ServerGUIDisplay.appendText("Registered Users: " + result);
    }

    public void QueryLockedOutUsers(MouseEvent mouseEvent) throws SQLException { //Show List of Locked Out Users
        if(!ServerRunning) {
            alertServer("Server is not running", "");
            return;
        }
        clearDisplay(ServerGUIDisplay);
        String result = Server.getDB().DBQueryLockedOutUsers();
        if(result.equals("0")) {
            ServerGUIDisplay.appendText("No Locked Out Users");
            return;
        }
        String[] users = result.split(",");
        ServerGUIDisplay.appendText("Locked Out Users" + "\n");
        for(String user : users) {
            ServerGUIDisplay.appendText(user + "\n");
        }
    }



    public void clearDisplay(TextArea Display) { Display.clear(); }

    public void alertServer(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Server Status");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public void StartServer(MouseEvent mouseEvent) {
        if(ServerRunning == false) {
            Server = new Server();
            ServerThread = new Thread(Server);
            ServerThread.start();
            ServerRunning = true;
        } else {
           alertServer("Server is already running", "");
        }
        StartServerButton.setText("Server is running...");
    }
}
