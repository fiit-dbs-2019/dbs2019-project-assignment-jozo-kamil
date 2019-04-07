package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Car;
import model.Contract;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import persistancemanagers.CarManager;
import persistancemanagers.EnumManager;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeAddHarmRepairController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private JFXTextField textFieldService;
    @FXML private JFXTextField textFieldPrice;
    @FXML private JFXDatePicker datePickerDate;

    private Contract contract;

    // name and loction of all services
    ObservableList<String> allServices = null;

    private String typeOfHarm;

    private boolean repairAdded = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // setters for data

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
        setServiceNamesAndLocations();
    }

    public void setTypeOfHarm(String typeOfHarm) {
        this.typeOfHarm = typeOfHarm;
    }

    public void setServiceNamesAndLocations() {
        EnumManager enumManager = new EnumManager();
        allServices = enumManager.getServiceNamesAndRecords();

        TextFields.bindAutoCompletion(textFieldService,allServices);
    }

    public boolean isRepairAdded() {
        return repairAdded;
    }

    // fields and checkers

    public String getServiceName() {
        return textFieldService.getText();
    }

    public Date getDateOfService() {
        if(datePickerDate.getValue() == null) {
            return null;
        }
        java.sql.Date date = java.sql.Date.valueOf(datePickerDate.getValue());
        return date;
    }

    public Float getPriceOfService() {
        return Float.parseFloat(textFieldPrice.getText());
    }

    public boolean checkFieldsBeforeSubmittingNewRepair() {
        if(getServiceName().trim().isEmpty() ||
                getDateOfService() == null) {
            return true;
        } else {
            try {
                Float.parseFloat(getPriceOfService().toString());
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    // button
    public void btnAddRepairPushed(ActionEvent actionEvent) {

        if(checkFieldsBeforeSubmittingNewRepair()) {
            Notifications notification = Notifications.create()
                    .title("Nesprávne vyplnené údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        } else {

            CarManager carManager = new CarManager();

            Car carFromContract = null;

            try {
                carFromContract = carManager.getCarFromDatabase(contract.getCar_vin());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(!carManager.addNewServisToSpecificCar(carFromContract,contract.getHarm_id(),getServiceName(),
                    typeOfHarm,getDateOfService(),getPriceOfService())){
                Notifications notification = Notifications.create()
                        .title("Nesprávny servis!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showError();
            } else {
                Notifications notification = Notifications.create()
                        .title("Oprava škody bola zaznamenaná!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showConfirm();

                repairAdded = true;
            }
        }
    }
}
