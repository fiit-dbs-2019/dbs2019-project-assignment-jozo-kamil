package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeAddCarController implements Initializable {

    @FXML private ComboBox comboBoxBrand;
    @FXML private ComboBox comboBoxModel;
    @FXML private Spinner spinnerMileage;
    @FXML private ComboBox comboBoxFuel;
    @FXML private ComboBox comboBoxGearBox;
    @FXML private ComboBox comboBoxCarBody;
    @FXML private ComboBox comboBocColor;
    @FXML private Spinner spinnerPrice;
    @FXML private AnchorPane rootPane;
    @FXML private DatePicker datePickerYear;
    @FXML private Spinner spinnerEngineCapacity;
    @FXML private Spinner spinnerEnginePower;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnConfirmPushed(ActionEvent actionEvent) {
    }
}
