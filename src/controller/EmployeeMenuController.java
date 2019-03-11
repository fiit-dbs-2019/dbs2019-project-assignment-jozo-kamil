package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EmployeeMenuController implements Initializable {


    public Button ee;
    public Button aa;
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelSecondName;
    @FXML private Label labelDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        labelDate.setText(time);
        ee.setDisable(true);
        aa.setDisable(true);
    }

    public void btnSearchingPushed(ActionEvent actionEvent) throws IOException {

    }

    public void btnCreateCarPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_car.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnCreateAgreement(ActionEvent actionEvent) throws IOException {

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
