package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCarController implements Initializable {


    public ChoiceBox chboxBrand;
    public ChoiceBox chboxModel;
    public DatePicker datePickerYear;
    public TextField textFieldMileage;
    public ChoiceBox chboxFuel;
    public Spinner spinnerEngineCapacity;
    public Spinner spinnerEnginePower;
    public ChoiceBox chboxGearBox;
    public ChoiceBox chboxCarBody;
    public ChoiceBox chboxColor;
    public TextField textFieldLicensePlate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnBackPushed(ActionEvent actionEvent) {
    }

    public void btnConfirmPushed(ActionEvent actionEvent) {
    }
}
