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

public class ServerController extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent ServerGUI = FXMLLoader.load(getClass().getResource("/ServerGUI.fxml")); //Server GUI
        Scene scene = new Scene(ServerGUI);
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        Server Server = new Server();
        Server.run();
    }


    @FXML private TextArea ServerGUIDisplay;

    public void QueryConnectedUsers(MouseEvent mouseEvent) {
//        int connectedUsers = Server.getConnections();
//        ServerGUIDisplay.setText("" + connectedUsers);
    }

    public void QueryLoggedInUsers(MouseEvent mouseEvent) {

    }

    public void QuerySignedUpUsers(MouseEvent mouseEvent) {

    }

    public void QueryRegisteredUsers(MouseEvent mouseEvent) {

    }

    public void QueryLockedOutUsers(MouseEvent mouseEvent) {

    }
}
