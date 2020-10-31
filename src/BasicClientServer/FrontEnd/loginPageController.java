package BasicClientServer.FrontEnd;

import BasicClientServer.BackEnd.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class loginPageController {

    @FXML private TextField passwordShown;
    @FXML private PasswordField passwordHidden;
    @FXML private CheckBox showPasswordCheckBox;



    //Opens Account Creation window upon pressing 'New Account' Button
    public void createAccountWindow(MouseEvent mouseEvent) {
        try {
            Parent newAccount = FXMLLoader.load(getClass().getResource("/NewAccount.fxml")); //account creation page
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Account");
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); //Setting the window in the middle of main window
            stage.setX((screenBounds.getWidth() - 600) / 2);
            stage.setY((screenBounds.getHeight() - 600) / 2);
            stage.setScene(new Scene(newAccount, 600, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Opens Password Recovery window upon pressing 'Forgot Password' Button
    public void forgotPasswordWindow(MouseEvent mouseEvent) {
        try {
            Parent newAccount = FXMLLoader.load(getClass().getResource("/ForgotPassword.fxml")); //account creation page
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Password Recovery");
            stage.setScene(new Scene(newAccount, 484, 299));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Logic for the show password checkbox
    public void togglePasswordVisibility(ActionEvent actionEvent) {
        if(showPasswordCheckBox.isSelected()){
            System.out.println("password is now shown");
            passwordShown.setText(passwordHidden.getText());
            passwordShown.setVisible(true);
            passwordHidden.setVisible(false);
            return;
        } else {
            System.out.println("password is now hidden");
            passwordHidden.setText(passwordShown.getText());
            passwordHidden.setVisible(true);
            passwordShown.setVisible(false);
        }


    }

    //Connects to server upon pressing 'Connect' Button
    public void connectToServer(MouseEvent mouseEvent) {
        String host = "127.0.0.1";
        int port = 8000;
        // -- instantiate a Client object
        //    the constructor will attempt to connect to the server
        Client client = new Client(host, port);
    }





}
