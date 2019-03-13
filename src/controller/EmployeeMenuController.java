package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import model.Employee;
import persistancemanagers.EmployeeManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmployeeMenuController implements Initializable {

    public Button ee;
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;

    public void setHeader () {
        EmployeeManager em = new EmployeeManager();
        try {
            em.setHeader(labelFirstName,labelLastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setHeader();
        ee.setDisable(true);
    }

    public void btnSearchingPushed(ActionEvent actionEvent) throws IOException {

    }

    public void btnCreateCarPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_car.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnCreateAgreement(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_create_contract.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnLogoutPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnAddCustomerPushed(ActionEvent actionEvent) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_customer.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
