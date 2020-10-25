module ClientServerApplication {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;

    opens BasicClientServer;
    opens BasicClientServer.FrontEnd to javafx.fxml;
    opens BasicClientServer.BackEnd to javafx.fxml;
    opens BasicClientServer.resources to javafx.fxml;

}