package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Employee;
import persistancemanagers.EmployeeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TextField fieldLogin;
    @FXML private PasswordField fieldPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public String getLogin() {
        return fieldLogin.getText();
    }

    public String getPassword() { return fieldPassword.getText(); }

    public void log_in(ActionEvent actionEvent) throws SQLException, IOException {
        Employee employee = null;

        EmployeeManager em = new EmployeeManager();
        employee = em.LoginEngine(getLogin(),getPassword());

        if (employee == null) {
            System.out.println("Bad combination. Try it another time!");
        } else {
            if (employee.getType().equals("admin")) {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
                rootPane.getChildren().setAll(pane);
            } else {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
                rootPane.getChildren().setAll(pane);
            }
        }
    }
}
