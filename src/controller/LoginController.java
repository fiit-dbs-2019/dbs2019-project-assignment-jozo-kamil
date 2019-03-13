package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import model.Employee;
import persistancemanagers.EmployeeManager;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TextField fieldLogin;
    @FXML private PasswordField fieldPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public String getLogin() {
        return fieldLogin.getText();
    }

    public String getPassword() { return fieldPassword.getText(); }

    public void logIn(ActionEvent actionEvent) throws SQLException, IOException {
        Employee employee = null;

        EmployeeManager em = new EmployeeManager();
        employee = em.LoginEngine(getLogin(),getPassword());

        if (employee == null) {
            Alert alertBadLogin = new Alert(Alert.AlertType.ERROR,"Nesprávne prihlasovacie údaje", ButtonType.CLOSE);
            alertBadLogin.show();
        } else {

            // add logged employeeID to properties file
            Properties prop = new Properties();
            OutputStream output = null;

            FileInputStream input = new FileInputStream("src/properties");
            prop.load(input);

            input = new FileInputStream("src/properties");
            prop.load(input);

            try {
                output = new FileOutputStream("src/properties");

                prop.setProperty("loggedID", String.valueOf(employee.getEmployeeID()));

                prop.store(output, null);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

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
