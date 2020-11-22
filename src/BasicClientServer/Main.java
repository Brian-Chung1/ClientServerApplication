package BasicClientServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //test push
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loginPage = FXMLLoader.load(getClass().getResource("resources/LoginPage.fxml")); //login page
        Parent newAccount = FXMLLoader.load(getClass().getResource("resources/AccountRegistration.fxml")); //account creation page
        Parent forgotPassword = FXMLLoader.load(getClass().getResource("resources/PasswordRecovery.fxml")); //password recovery page
        Parent changePassword = FXMLLoader.load(getClass().getResource("resources/ChangePassword.fxml")); //password change page
        Parent mainApplication = FXMLLoader.load(getClass().getResource("resources/MainApplication.fxml")); //Main Application
        Parent connectWindow = FXMLLoader.load(getClass().getResource("resources/connectWindow.fxml")); //Main Application


        Scene scene = new Scene(connectWindow);

        primaryStage.setTitle("Connect");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
