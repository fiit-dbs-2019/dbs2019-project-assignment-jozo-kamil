package controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Employee;
import org.controlsfx.control.Notifications;
import persistancemanagers.CarManager;
import persistancemanagers.EmployeeManager;

import javax.management.Notification;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private Button buttonLogIn;

    @FXML private JFXTextField fieldLogin;
    @FXML private JFXPasswordField fieldPassword;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonLogIn.setDefaultButton(true);

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(2000));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.play();
    }

    public String getLogin() {
        return fieldLogin.getText();
    }

    public String getPassword() { return fieldPassword.getText(); }

    public void logIn(ActionEvent actionEvent) throws SQLException {

        if(getLogin().trim().isEmpty() || getPassword().trim().isEmpty()) {

            Notifications notification = Notifications.create()
                    .title("Zadaj údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showWarning();
            return;
        }

        Employee employee = null;

        EmployeeManager em = new EmployeeManager();
        employee = em.LoginEngine(getLogin(),getPassword());

        if (employee == null) {

            Notifications notification = Notifications.create()
                    .title("Nesprávne prihlasovacie údaje")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
            return;
        }

        if (employee.getType().equals("admin")) {

            setNewScene("../view/admin_menu.fxml",employee);

            Notifications notification = Notifications.create()
                    .title("Prihlásenie bolo úspešné!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showConfirm();
        } else {

            setNewScene("../view/employee_menu.fxml",employee);

            Notifications notification = Notifications.create()
                    .title("Prihlásenie bolo úspešné!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showConfirm();
        }
    }

    public void setNewScene(String nextScene, Employee employee) {

        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource(nextScene));
            parent = (Parent) loaader.load();


            if(employee.getType().equals("admin")) {
                AdminMenuController adminMenuController = loaader.getController();
                adminMenuController.setAdmin(employee);
            } else {
                EmployeeMenuController employeeMenuController = loaader.getController();
                employeeMenuController.setEmployee(employee);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }
}
