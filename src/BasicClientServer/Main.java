package BasicClientServer;

import BasicClientServer.BackEnd.Client;
import BasicClientServer.FrontEnd.ClientGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ClientGUIController C;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loginPage = FXMLLoader.load(getClass().getResource("resources/LoginPage.fxml")); //login page
        Parent newAccount = FXMLLoader.load(getClass().getResource("resources/AccountRegistration.fxml")); //account creation page
        Parent forgotPassword = FXMLLoader.load(getClass().getResource("resources/PasswordRecovery.fxml")); //password recovery page
        Parent changePassword = FXMLLoader.load(getClass().getResource("resources/ChangePassword.fxml")); //password change page
        Parent mainApplication = FXMLLoader.load(getClass().getResource("resources/MainApplication.fxml")); //Main Application
        Parent connectWindow = FXMLLoader.load(getClass().getResource("resources/connectWindow.fxml")); //Main Application

        //login page - disconnect
        //main app - disconnect logout
        Scene scene = new Scene(connectWindow);

        primaryStage.setTitle("Connect");
        primaryStage.setScene(scene);
        primaryStage.show();
        C = new ClientGUIController();
    }

    @Override
    public void stop() {
        Client client = C.getClient();
        client.networkaccess.sendString("logout", false);
        client.networkaccess.sendString("disconnect", false);
        System.exit(0);
    }




    public static void main(String[] args) {
        launch(args);
    }

}
