package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
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
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/employee_menu.fxml"));
                Parent parent = loader.load();

                Scene tableViewScene = new Scene(parent);

                //access the controller and call a method
                EmployeeMenuController controller = loader.getController();
                controller.initData(employee);

                //This line gets the Stage information
                Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

                window.setScene(tableViewScene);
                window.show();
            }
        }
    }
}
