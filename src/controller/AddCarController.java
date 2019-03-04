package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCarController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private ChoiceBox chboxBrand;
    @FXML private ChoiceBox chboxModel;
    @FXML private DatePicker datePickerYear;
    @FXML private TextField textFieldMileage;
    @FXML private ChoiceBox chboxFuel;
    @FXML private Spinner spinnerEngineCapacity;
    @FXML private Spinner spinnerEnginePower;
    @FXML private ChoiceBox chboxGearBox;
    @FXML private ChoiceBox chboxCarBody;
    @FXML private ChoiceBox chboxColor;
    @FXML private TextField textFieldLicensePlate;

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
