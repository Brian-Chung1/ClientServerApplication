package BasicClientServer.FrontEnd;

import BasicClientServer.BackEnd.Client;
import BasicClientServer.BackEnd.NetworkAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class connectWindowController {

    public Client client;
    public boolean successfulConnect;
    @FXML private TextField IPTextField;


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

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            };

            // -- send a String to the server and wait for the response
            commandString = "connect";
            replyString = client.networkaccess.sendString(commandString, true);
            System.out.println(replyString);
            if(replyString.equals("Success")) {
                successfulConnect = true;
            } else {
                successfulConnect = false;
            }



        if(successfulConnect) {
            try {
                Parent newAccount = FXMLLoader.load(getClass().getResource("/LoginPage.fxml")); //account creation page
                Stage stage = new Stage();
                stage.setTitle("Login Page");
                stage.setScene(new Scene(newAccount, 721, 475));
                ((Stage)((mouseEvent.getSource()).getScene().getWindow())).close();
                stage.show();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //display an error message
            System.out.println("error");
        }
    }

    public static void main(String[] args) {

    }
}
