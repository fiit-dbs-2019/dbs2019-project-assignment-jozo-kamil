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

    @FXML private AnchorPane rootPane;
    @FXML private PasswordField fieldPassword;
    @FXML private TextField fieldLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void log_in(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
