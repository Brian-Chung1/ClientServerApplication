module ClientServerApplication {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires java.sql;
    requires java.mail;

    exports BasicClientServer.BackEnd;

    opens BasicClientServer;
    opens BasicClientServer.FrontEnd to javafx.fxml;
    opens BasicClientServer.BackEnd to javafx.fxml;

}