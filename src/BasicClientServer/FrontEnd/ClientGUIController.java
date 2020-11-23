package BasicClientServer.FrontEnd;

import BasicClientServer.BackEnd.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class ClientGUIController {

    //Connect Window Elements
    public static Client client;
    @FXML private TextField IPTextField;

    //Login Page Elements
    @FXML private TextField passwordShown;
    @FXML private PasswordField passwordHidden;
    @FXML private CheckBox showPasswordCheckBox;
    @FXML private TextField LoginPageUsernameTextField;

    //Change Password Elements
    @FXML private TextField ChangePasswordUsernameTextField;
    @FXML private TextField oldPasswordShown;
    @FXML private PasswordField oldPasswordHidden;
    @FXML private TextField newPasswordShown;
    @FXML private PasswordField newPasswordHidden;
    @FXML private TextField ReEnteredNewPasswordShown;
    @FXML private PasswordField ReEnteredNewPasswordHidden;
    @FXML private CheckBox showPasswordCheckBoxCP;

    //Password Recovery Elements
    @FXML private TextField PasswordRecoveryTextField;

    //Account Registration Elements
    @FXML private TextField NewAccountEmailTextField;
    @FXML private PasswordField NewAccountPasswordHidden;
    @FXML private CheckBox showPasswordCheckBoxAR;
    @FXML private TextField NewAccountPasswordShown;
    @FXML private TextField NewAccountUsernameTextField;





    //Login Page Controller -------------------------------------------------------------------------------------------------------------------------
    //Opens Account Creation window upon pressing 'New Account' Button
    public void createAccountWindow(MouseEvent mouseEvent) {
        try {
            Parent newAccount = FXMLLoader.load(getClass().getResource("/AccountRegistration.fxml")); //account creation page
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
            Parent newAccount = FXMLLoader.load(getClass().getResource("/PasswordRecovery.fxml")); //account creation page
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

    //Disconnects from server upon pressing 'Disconnect' Button
    public void disconnectFromServer(MouseEvent mouseEvent) {
        System.out.println("pressed disconnect");
        client.disconnect();

        //close the GUI
        ((Stage)(((Button)mouseEvent.getSource()).getScene().getWindow())).close();
    }

    public void AccountLoginEvent(MouseEvent mouseEvent) {
        // -- send message to server and receive reply.
        String commandString;
        String replyString;

        //If the User presses login button with nothing typed in the fields then popup error message to type in shit
        String username = LoginPageUsernameTextField.getText();
        String password = passwordHidden.getText().length() > passwordShown.getText().length() ? passwordHidden.getText() : passwordShown.getText();

        commandString = "login" + "," + username + "," + password;

        replyString = client.networkaccess.sendString(commandString, true);

        if(replyString.equals("Success")) {
            try {
                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
                Parent loginPage = FXMLLoader.load(getClass().getResource("/MainApplication.fxml")); //main application page
                Stage stage = new Stage();
                stage.setTitle("Main Application");
                stage.setScene(new Scene(loginPage, 753, 497));
                stage.show();

            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Caught exception");
            }
        } else {
            //display an error message
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Login Error");
            error.setContentText("Please Re-Enter your Username and Password");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------

    //Connect Window Controller -----------------------------------------------------------------------------------------------
    public void connectToServer(MouseEvent mouseEvent) {
        String host = IPTextField.getText();
        if(host == "") return;
        int port = 8000;
        // -- instantiate a Client object
        //    the constructor will attempt to connect to the server
        client = new Client(host, port);

        // -- send message to server and receive reply.
        String commandString;
        String replyString;

        // -- send a String to the server and wait for the response
        commandString = "connect";
        replyString = client.networkaccess.sendString(commandString, true);
        System.out.println(replyString);
        if(replyString.equals("Success")) {
            try {
                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
                Parent loginPage = FXMLLoader.load(getClass().getResource("/LoginPage.fxml")); //account creation page
                Stage stage = new Stage();
                stage.setTitle("Login Page");
                stage.setScene(new Scene(loginPage, 721, 475));
                stage.show();

            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Caught exception");
            }
        } else {
            //display an error message
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Connection Error");
            error.setContentText("Faulty Internet or Incorrect IP Address - Please Check Your Connection or IP Address");
            error.setHeaderText(null);
            error.showAndWait();
        }



    }


    //-------------------------------------------------------------------------------------------------------------------------

    //Change Password Window Controller ---------------------------------------------------------------------------------------
    public void togglePasswordVisibilityCP(ActionEvent actionEvent) {
        if(showPasswordCheckBoxCP.isSelected()){
            System.out.println("password is now shown");
            oldPasswordShown.setText(oldPasswordHidden.getText());
            newPasswordShown.setText(newPasswordHidden.getText());
            ReEnteredNewPasswordShown.setText(ReEnteredNewPasswordHidden.getText());
            oldPasswordHidden.setVisible(false);
            newPasswordHidden.setVisible(false);
            ReEnteredNewPasswordHidden.setVisible(false);
            oldPasswordShown.setVisible(true);
            newPasswordShown.setVisible(true);
            ReEnteredNewPasswordShown.setVisible(true);
        } else {
            System.out.println("password is now hidden");
            oldPasswordHidden.setText(oldPasswordShown.getText());
            newPasswordHidden.setText(newPasswordShown.getText());
            ReEnteredNewPasswordHidden.setText(ReEnteredNewPasswordShown.getText());
            oldPasswordHidden.setVisible(true);
            newPasswordHidden.setVisible(true);
            ReEnteredNewPasswordHidden.setVisible(true);
            oldPasswordShown.setVisible(false);
            newPasswordShown.setVisible(false);
            ReEnteredNewPasswordShown.setVisible(false);
        }
    }

    public void ChangePasswordEvent(MouseEvent mouseEvent) {
        String username = ChangePasswordUsernameTextField.getText();
        String oldPassword = oldPasswordHidden.getText().length() > oldPasswordShown.getText().length() ? oldPasswordHidden.getText() : oldPasswordShown.getText();
        String newPassword = newPasswordHidden.getText().length() > newPasswordShown.getText().length() ? newPasswordHidden.getText() : newPasswordShown.getText();
        String ReEnteredNewPassword = ReEnteredNewPasswordHidden.getText().length() > ReEnteredNewPasswordShown.getText().length() ? ReEnteredNewPasswordHidden.getText() : ReEnteredNewPasswordShown.getText();

        //if the fields are empty or missing we return an error message
        if(username.isBlank() || oldPassword.isBlank() || newPassword.isBlank() || ReEnteredNewPassword.isBlank()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Missing Fields");
            error.setContentText("Please Enter your information in all appropriate fields");
            error.setHeaderText(null);
            error.showAndWait();
            return;
        }

        //if the new password entries do not match we return an error message
        if(!newPassword.equals(ReEnteredNewPassword)) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Password Does Not Match");
            error.setContentText("Make sure that both New Password Entries Match");
            error.setHeaderText(null);
            error.showAndWait();
            return;
        }


    }




    // ------------------------------------------------------------------------------------------------------------------------


    //Password Recovery Window Controller ---------------------------------------------------------------------------------------
    public void PasswordRecoveryEvent(MouseEvent mouseEvent) {
        String userInfo = PasswordRecoveryTextField.getText();

    }



    // ------------------------------------------------------------------------------------------------------------------------


    //New Account Window Controller -------------------------------------------------------------------------------------------


    public void AccountRegistrationEvent(MouseEvent mouseEvent) {
        String email = NewAccountEmailTextField.getText();
        String username = NewAccountUsernameTextField.getText();
        String password = NewAccountPasswordHidden.getText().length() > NewAccountPasswordShown.getText().length() ? NewAccountPasswordHidden.getText() : NewAccountPasswordShown.getText();

        if(email.isBlank() || username.isBlank() || password.isBlank()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Missing Fields");
            error.setContentText("Please Enter your information in all appropriate fields");
            error.setHeaderText(null);
            error.showAndWait();
            return;
        }



    }

    public void togglePasswordVisibilityAR(MouseEvent mouseEvent) {
        if(showPasswordCheckBoxAR.isSelected()){
            System.out.println("password is now shown");
            NewAccountPasswordShown.setText(NewAccountPasswordHidden.getText());
            NewAccountPasswordShown.setVisible(true);
            NewAccountPasswordHidden.setVisible(false);
        } else {
            System.out.println("password is now hidden");
            NewAccountPasswordHidden.setText(NewAccountPasswordShown.getText());
            NewAccountPasswordHidden.setVisible(true);
            NewAccountPasswordShown.setVisible(false);
        }

    }



    // ------------------------------------------------------------------------------------------------------------------------


    //Main Application Window Controller ---------------------------------------------------------------------------------------
    //Calls Disconnect From Server Method Located in Login Page Controller Tab
    //Create new function that calls log out and then it will call disconnect

    public void AccountLogoutEvent(MouseEvent mouseEvent) {
        // -- send message to server and receive reply.
        String commandString;
        String replyString;

        commandString = "logout";

        replyString = client.networkaccess.sendString(commandString, true);

        //Change this later - this was copied and pasted from elsewhere as boilerplate
//        if(replyString.equals("success")) {
//            try {
//                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
//                Parent loginPage = FXMLLoader.load(getClass().getResource("/MainApplication.fxml")); //main application page
//                Stage stage = new Stage();
//                stage.setTitle("Main Application");
//                stage.setScene(new Scene(loginPage, 721, 475));
//                stage.show();
//
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Caught exception");
//            }
//        } else {
//            //display an error message
//            Alert error = new Alert(Alert.AlertType.ERROR);
//            error.setTitle("Login Error");
//            error.setContentText("Please Re-Enter your Username and Password");
//            error.setHeaderText(null);
//            error.showAndWait();
//        }
    }

    public void changePasswordWindow(MouseEvent mouseEvent) {
        try {
            Parent newAccount = FXMLLoader.load(getClass().getResource("/ChangePassword.fxml")); //account creation page
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Change Password");
            stage.setScene(new Scene(newAccount, 685, 485));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------
}
