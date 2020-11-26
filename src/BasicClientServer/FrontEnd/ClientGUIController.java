package BasicClientServer.FrontEnd;

import BasicClientServer.BackEnd.Client;
import BasicClientServer.BackEnd.RegexValidation;
import BasicClientServer.BackEnd.SendEmailGmailSMTP;
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
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class ClientGUIController {

    //Connect Window Elements
    public static Client client;
    @FXML private TextField IPTextField;

    //Login Page Elements
    @FXML private TextField loginPasswordShown;
    @FXML private PasswordField loginPasswordHidden;
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
        loadWindow("/AccountRegistration.fxml", "New Account", 600, 400);
    }

    //Opens Password Recovery window upon pressing 'Forgot Password' Button
    public void forgotPasswordWindow(MouseEvent mouseEvent) {
        loadWindow("/PasswordRecovery.fxml", "Password Recovery", 484, 299);
    }

    //Logic for the show password checkbox
    public void togglePasswordVisibility(ActionEvent actionEvent) {
        if(showPasswordCheckBox.isSelected()){
            swapPasswordTextField(loginPasswordHidden, loginPasswordShown, true);
        } else {
            swapPasswordTextField(loginPasswordHidden, loginPasswordShown, false);
        }


    }

    //Disconnects from server upon pressing 'Disconnect' Button
    public void disconnectFromServer(MouseEvent mouseEvent) {
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
        String password = getLatestPassword(loginPasswordHidden, loginPasswordShown);

        if(username.isBlank() || password.isBlank()) {
            alertError("Missing Fields", "Please Enter your information in all appropriate fields");
            return;
        }

        commandString = "login" + "," + username + "," + password;

        replyString = client.networkaccess.sendString(commandString, true);

        if(replyString.equals("Success")) {
            ((Stage)(((Button)mouseEvent.getSource()).getScene().getWindow())).close();
            loadWindow("/MainApplication.fxml", "Main Application", 753, 497);
        } else if(replyString.equals("Locked")) {
            alertError("Account Locked Out", "Your account is locked out after 3 failed attempts \n Please go to Password Recovery to reset your Account");

        } else {
            alertError("Login Error", "Please Re-Enter your Username and Password");
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
            ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
            loadWindow("/LoginPage.fxml", "Login Page", 721, 475);
        } else {
            alertError("Connection Error", "Faulty Internet or Incorrect IP Address - Please Check Your Connection or IP Address");
        }



    }


    //-------------------------------------------------------------------------------------------------------------------------

    //Change Password Window Controller ---------------------------------------------------------------------------------------
    public void togglePasswordVisibilityCP(ActionEvent actionEvent) {
        if(showPasswordCheckBoxCP.isSelected()){
            swapPasswordTextField(oldPasswordHidden, oldPasswordShown, true);
            swapPasswordTextField(newPasswordHidden, newPasswordShown, true);
            swapPasswordTextField(ReEnteredNewPasswordHidden, ReEnteredNewPasswordShown, true);
        } else {
            swapPasswordTextField(oldPasswordHidden, oldPasswordShown, false);
            swapPasswordTextField(newPasswordHidden, newPasswordShown, false);
            swapPasswordTextField(ReEnteredNewPasswordHidden, ReEnteredNewPasswordShown, false);
        }
    }

    public void ChangePasswordEvent(MouseEvent mouseEvent) {
        String username = ChangePasswordUsernameTextField.getText();
        String oldPassword = getLatestPassword(oldPasswordHidden, oldPasswordShown);
        String newPassword = getLatestPassword(newPasswordHidden, newPasswordShown);
        String ReEnteredNewPassword = getLatestPassword(ReEnteredNewPasswordHidden, ReEnteredNewPasswordShown);

        //if the fields are empty or missing we return an error message
        if(username.isBlank() || oldPassword.isBlank() || newPassword.isBlank() || ReEnteredNewPassword.isBlank()) {
            alertError("Missing Fields", "Please Enter your information in all appropriate fields");
            return;
        }

        //if the new password entries do not match we return an error message
        if(!newPassword.equals(ReEnteredNewPassword)) {
            alertError("Passwords do not match", "Make sure that both New Password Entries Match");
            return;
        }

        if(!RegexValidation.validSimplePassword(newPassword)) {
            alertInformation("Invalid Format", "Password Incorrect Format", " It must have at least one digit \n It must have at least one upper or lower case letter \n It must be at least 8 characters long", false, null);
            return;
        }

        String commandString;
        String replyString;

        // -- send a String to the server and wait for the response
        commandString = "changepassword" + "," + username + "," + oldPassword + "," + newPassword;
        replyString = client.networkaccess.sendString(commandString, true);

        if(replyString.equals("Success")) {
            alertInformation("Success", "Password Successfully Changed", "", true, mouseEvent);
        } else if(replyString.equals("WrongOldPassword")) {
            alertError("Invalid Password", "The password you entered for your account is incorrect");
        } else if(replyString.equals("WrongUsername")) {
            alertError("Invalid Username", "Username does not exist");
        }

    }




    // --------------------------------------------------------------------------------------------------------------------------


    //Password Recovery Window Controller ---------------------------------------------------------------------------------------
    public void PasswordRecoveryEvent(MouseEvent mouseEvent) {
        String username = PasswordRecoveryTextField.getText();

        if(username.isBlank()) {
            alertError("Missing Fields", "Please Enter your information in all appropriate fields");
            return;
        }

        String commandString;
        String replyString;
        commandString = "forgotpassword" + "," + username;
        replyString = client.networkaccess.sendString(commandString, true);

        if(replyString.equals("Success")) {
            alertInformation("Success", "Email has been sent", "", true, mouseEvent);
        } else {
            alertError("Error", "Please re-enter your username and make sure it is valid");
        }

    }



    // ------------------------------------------------------------------------------------------------------------------------


    //New Account Window Controller -------------------------------------------------------------------------------------------


    public void AccountRegistrationEvent(MouseEvent mouseEvent) {
        String email = NewAccountEmailTextField.getText();
        String username = NewAccountUsernameTextField.getText();
        String password = getLatestPassword(NewAccountPasswordHidden, NewAccountPasswordShown);

        if(email.isBlank() || username.isBlank() || password.isBlank()) {
            alertError("Missing Fields", "Please Enter your information in all appropriate fields");
            return;
        }

        if(!RegexValidation.validEmailAddress(email)) {
            alertInformation("Invalid Format", "Email Address Incorrect Format", "Make sure the email address is valid and properly typed", false, null);
            return;
        }

        if(!RegexValidation.validSimplePassword(password)) {
            alertInformation("Invalid Format", "Password Incorrect Format", " It must have at least one digit \n It must have at least one upper or lower case letter \n It must be at least 8 characters long", false, null);
            return;
        }

        String commandString;
        String replyString;

        // -- send a String to the server and wait for the response
        commandString = "newaccount" + "," + email + "," + username + "," + password;
        replyString = client.networkaccess.sendString(commandString, true);

        if(replyString.equals("Success")) {
            alertInformation("Success", "Your account has been created", "", true, mouseEvent);
        } else {
            alertError("Account Creation Failed", "User with username: " + username + "already exists \n Please create a new account with a different username");
        }



    }

    public void togglePasswordVisibilityAR(MouseEvent mouseEvent) {
        if(showPasswordCheckBoxAR.isSelected()){
            swapPasswordTextField(NewAccountPasswordHidden, NewAccountPasswordShown, true);
        } else {
            swapPasswordTextField(NewAccountPasswordHidden, NewAccountPasswordShown, false);
        }

    }


    // ------------------------------------------------------------------------------------------------------------------------


    //Main Application Window Controller ---------------------------------------------------------------------------------------
    //Calls Disconnect From Server Method Located in Login Page Controller Tab

    public void disconnectAndLogout(MouseEvent mouseEvent) {
        String commandString;
        String replyString;
        commandString = "logout";
        replyString = client.networkaccess.sendString(commandString, false);
        ((Stage)(((Button)mouseEvent.getSource()).getScene().getWindow())).close();
        disconnectFromServer(mouseEvent);
    }

    public void AccountLogoutEvent(MouseEvent mouseEvent) {
        // -- send message to server and receive reply.
        String commandString;
        String replyString;

        commandString = "logout";
        replyString = client.networkaccess.sendString(commandString, false);
        ((Stage)(((Button)mouseEvent.getSource()).getScene().getWindow())).close();
        loadWindow("/LoginPage.fxml", "Login Page", 721, 475);
    }

    public void changePasswordWindow(MouseEvent mouseEvent) {
        loadWindow("/ChangePassword.fxml", "Change Password", 685, 485);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    public void alertError(String title, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setContentText(message);
        error.setHeaderText(null);
        error.showAndWait();
    }

    public void alertInformation(String title, String headerText, String contentText, boolean closingEvent, MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        if(closingEvent) {
            if(result.get() == ButtonType.OK || !result.isPresent()) {
                ((Stage)(((Button)mouseEvent.getSource()).getScene().getWindow())).close();
            }
        }
    }

    public void loadWindow(String fileLocation, String windowTitle, int width, int height) {
        try {
            Parent newAccount = FXMLLoader.load(getClass().getResource(fileLocation)); //account creation page
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(newAccount, width, height));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLatestPassword(PasswordField hidden, TextField shown) {
        return hidden.getText().length() > shown.getText().length() ? hidden.getText() : shown.getText();
    }

    public void swapPasswordTextField(PasswordField hidden, TextField shown, boolean flag) {
        if(flag) {
            shown.setText(hidden.getText());
            hidden.setVisible(false);
            shown.setVisible(true);
        } else {
            hidden.setText(shown.getText());
            hidden.setVisible(true);
            shown.setVisible(false);
        }

    }



}
