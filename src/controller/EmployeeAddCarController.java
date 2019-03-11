package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import persistancemanagers.EnumManager;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeAddCarController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private ComboBox comboBoxBrand;
    @FXML private ComboBox comboBoxModel;
    @FXML private Spinner spinnerMileage;
    @FXML private ComboBox comboBoxFuel;
    @FXML private ComboBox comboBoxGearBox;
    @FXML private ComboBox comboBoxCarBody;
    @FXML private ComboBox comboBoxColor;
    @FXML private Spinner spinnerPrice;
    @FXML private DatePicker datePickerYear;
    @FXML private Spinner spinnerEngineCapacity;
    @FXML private Spinner spinnerEnginePower;
    @FXML private TextField textFieldSpz;
    @FXML private TextField textFieldVin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerConfig();
        addItemsComboBox();
    }

    public void addItemsComboBox() {
        EnumManager em = new EnumManager();
        try {
            em.employeeTypeEnum(comboBoxBrand,"car_brand");
            em.employeeTypeEnum(comboBoxCarBody,"car_body_style");
            em.employeeTypeEnum(comboBoxGearBox,"car_gear_box");
            em.employeeTypeEnum(comboBoxFuel,"car_fuel");
            em.employeeTypeEnum(comboBoxColor,"car_color");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void spinnerConfig() {
        spinnerEngineCapacity.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(2,3.6,2,0.1));
        spinnerEnginePower.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(80,280,180,1));
        spinnerMileage.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,10000,1000));
        spinnerPrice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20,120,50,1));
    }

    // getters for fields and spinners in gui
    public String getBrand() {
        return comboBoxBrand.getSelectionModel().getSelectedItem().toString();
    }

    public String getModel() {
        return comboBoxModel.getSelectionModel().getSelectedItem().toString();
    }

    public Integer getMileAge() {
        return (Integer) spinnerMileage.getValue();
    }

    public String getFuel() {
        return comboBoxFuel.getSelectionModel().getSelectedItem().toString();
    }

    public String getGearBox() {
        return comboBoxGearBox.getSelectionModel().getSelectedItem().toString();
    }

    public String getCarBody() {
        return comboBoxCarBody.getSelectionModel().getSelectedItem().toString();
    }

    public String getColor() {
        return comboBoxColor.getSelectionModel().getSelectedItem().toString();
    }

    public Integer getPrice() {
        return (Integer) spinnerPrice.getValue();
    }

    public Date getYear() {
        Date date = Date.valueOf(datePickerYear.getValue());
        return date;
    }

    public Float getEngineCapacity() {
        return (Float) spinnerEngineCapacity.getValue();
    }

    public Integer getEnginePower() {
        return (Integer) spinnerEnginePower.getValue();
    }

    public String getSPZ() {
        return textFieldSpz.getText();
    }

    public String getVIN() { return textFieldVin.getText(); }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnConfirmPushed(ActionEvent actionEvent) {
    }

    public void comboBoxModelClicked(MouseEvent mouseEvent) {
        // select na databazu
    }

}
