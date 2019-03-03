package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

<<<<<<< HEAD
    @FXML private AnchorPane rootPane;
    @FXML private PasswordField fieldPassword;
    @FXML private TextField fieldLogin;
=======

    @FXML private PasswordField fieldPassword;
    @FXML private TextField fieldLogin;
    @FXML private AnchorPane rootPane;
>>>>>>> 8318383446cd2d062c663b7380c09e97f76b4ca7

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void log_in(ActionEvent actionEvent) throws IOException {
<<<<<<< HEAD
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
=======
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
>>>>>>> 8318383446cd2d062c663b7380c09e97f76b4ca7
        rootPane.getChildren().setAll(pane);
    }
}
