package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javafx.stage.StageStyle;
import model.Employee;
import persistancemanagers.CarManager;
import persistancemanagers.EmployeeManager;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Button buttonLogIn;
    @FXML private AnchorPane rootPane;
    @FXML private TextField fieldLogin;
    @FXML private PasswordField fieldPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) { buttonLogIn.setDefaultButton(true);}

    public String getLogin() {
        return fieldLogin.getText();
    }

    public String getPassword() { return fieldPassword.getText(); }

    public void logIn(ActionEvent actionEvent) throws SQLException, IOException {

        if(getLogin().trim().isEmpty() || getPassword().trim().isEmpty()) {
            Alert alertEmptyField = new Alert(Alert.AlertType.WARNING,"Zadaj údaje!", ButtonType.CLOSE);
            alertEmptyField.initStyle(StageStyle.TRANSPARENT);
            alertEmptyField.setHeaderText("Varovanie!");
            alertEmptyField.showAndWait();
            return;
        }

        Employee employee = null;

        EmployeeManager em = new EmployeeManager();
        employee = em.LoginEngine(getLogin(),getPassword());

        if (employee == null) {
            Alert alertBadLogin = new Alert(Alert.AlertType.ERROR,"Nesprávne prihlasovacie údaje", ButtonType.CLOSE);
            alertBadLogin.initStyle(StageStyle.TRANSPARENT);
            alertBadLogin.setHeaderText("Chyba!");
            alertBadLogin.showAndWait();
        } else {

            // add logged employeeID to properties file
            Properties prop = new Properties();
            OutputStream output = null;

            // input for properties
            FileInputStream input = new FileInputStream("src/properties");
            prop.load(input);

            // input for contract_info
            Properties prop_contract = new Properties();
            input = new FileInputStream("src/contract_info");
            prop_contract.load(input);

//            input = new FileInputStream("src/properties");
//            prop.load(input);

            try {
                // output for properties
                output = new FileOutputStream("src/properties");

                prop.setProperty("loggedID", String.valueOf(employee.getEmployeeID()));

                prop.store(output, null);

                // output for contract_info
                output = new FileOutputStream("src/contract_info");

                prop_contract.setProperty("employeeID", String.valueOf(employee.getEmployeeID()));

                prop_contract.store(output,null);

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
