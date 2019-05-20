package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Car;
import model.ServiceRecord;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import persistancemanagers.CarManager;
import persistancemanagers.EnumManager;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class EmployeeCarDetailController implements Initializable {


    // noneditable attributes
    @FXML private TableView<Car> tableNonEditable;
    @FXML private TableColumn<Car, String> collumnVIN;
    @FXML private TableColumn<Car, Date> collumnYearOfProduction;
    @FXML private TableColumn<Car, String> collumnBrand;
    @FXML private TableColumn<Car, String> collumnModel;
    @FXML private TableColumn<Car, String> collumnBodyStyle;
    @FXML private TableColumn<Car, Float> collumnEngineCapacity;
    @FXML private TableColumn<Car, String> collumnGearBox;
    @FXML private TableColumn<Car, String> collumnsFuel;
    @FXML private TableColumn<Car, String> collumnColor;

    // editable attributes
    @FXML private TableView<Car> tableEditable;
    @FXML private TableColumn<Car, String> collumnSPZ;
    @FXML private TableColumn<Car, Integer> collumnMileAge;
    @FXML private TableColumn<Car, Integer> collumnEnginePower;
    @FXML private TableColumn<Car, Float> collumnPricePerDay;

    // service record table
    @FXML private TableView<ServiceRecord> tableServiceRecords;
    @FXML private TableColumn<ServiceRecord, String> collumnServisName;
    @FXML private TableColumn<ServiceRecord, String> collumnServisLocation;
    @FXML private TableColumn<ServiceRecord, String> collumnTypeOfRepair;
    @FXML private TableColumn<ServiceRecord, Date> collumnDateOfService;
    @FXML private TableColumn<ServiceRecord, Float> collumnsPriceOfService;

    @FXML private Label labelNumberOfServices;

    // field for adding new service record
    @FXML private JFXTextField textFieldServiceName;
    @FXML private JFXTextField textFieldTypeOfRepair;
    @FXML private JFXDatePicker datePickerDateOfService;
    @FXML private JFXTextField textFieldPriceOfService;

    private Car car;

    // name and loction of all services
    ObservableList<String> allServices = null;

    // type of repair
    ObservableList<String> allRepairs = null;

    private Boolean dataChanged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set noneditable table collumns
        collumnVIN.setCellValueFactory(new PropertyValueFactory<>("car_vin"));
        collumnYearOfProduction.setCellValueFactory(new PropertyValueFactory<>("year_of_production"));
        collumnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        collumnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        collumnBodyStyle.setCellValueFactory(new PropertyValueFactory<>("body_style"));
        collumnEngineCapacity.setCellValueFactory(new PropertyValueFactory<>("engine_capacity"));
        collumnGearBox.setCellValueFactory(new PropertyValueFactory<>("gear_box"));
        collumnsFuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        collumnColor.setCellValueFactory(new PropertyValueFactory<>("color"));

        // set editable table collumns
        collumnSPZ.setCellValueFactory(new PropertyValueFactory<>("spz"));
        collumnMileAge.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        collumnEnginePower.setCellValueFactory(new PropertyValueFactory<>("engine_power"));
        collumnPricePerDay.setCellValueFactory(new PropertyValueFactory<>("price_per_day"));

        tableEditable.setEditable(true);
        collumnSPZ.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnMileAge.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        collumnEnginePower.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        collumnPricePerDay.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        // set service records table collumns
        collumnServisName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        collumnServisLocation.setCellValueFactory(new PropertyValueFactory<>("serviceLocation"));
        collumnTypeOfRepair.setCellValueFactory(new PropertyValueFactory<>("typeOfRepair"));
        collumnDateOfService.setCellValueFactory(new PropertyValueFactory<>("dateOfService"));
        collumnsPriceOfService.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
    }

    public void setCar(Car car) {
        this.car = car;

        tableNonEditable.setItems(FXCollections.observableArrayList(car));
        tableEditable.setItems(FXCollections.observableArrayList(car));
        tableServiceRecords.setItems(car.getServiceRecords());

        setServiceNamesAndLocations();
        setRepairTypes();
        setNumberOfServices();
    }

    public void setServiceNamesAndLocations() {
        EnumManager enumManager = new EnumManager();
        allServices = enumManager.getServiceNamesAndRecords();

        TextFields.bindAutoCompletion(textFieldServiceName,allServices);
    }

    public void setRepairTypes() {
        EnumManager enumManager = new EnumManager();
        allRepairs = enumManager.getTypeOfHarm();

        TextFields.bindAutoCompletion(textFieldTypeOfRepair,allRepairs);
    }

    public void setEditation(Boolean editationEnabled) {
        tableEditable.setEditable(editationEnabled);
    }

    // if data were changed
    public Boolean getDataChanged() {
        return dataChanged;
    }

    public String getServiceName() {
        return textFieldServiceName.getText();
    }

    public String getTypeOfRepair() { return textFieldTypeOfRepair.getText(); }

    public Date getDateOfService() {
        if(datePickerDateOfService.getValue() == null) {
            return null;
        }
        java.sql.Date date = java.sql.Date.valueOf(datePickerDateOfService.getValue());
        return date;
    }

    public Float getPriceOfService() {
        return Float.parseFloat(textFieldPriceOfService.getText());
    }

    public void removeTypedInfo() {
        textFieldServiceName.setText("");
        textFieldTypeOfRepair.setText("");
        datePickerDateOfService.getEditor().clear();
        textFieldPriceOfService.setText("");
    }

    public void setNumberOfServices() {
        labelNumberOfServices.setText("Počet záznamov: " + car.getServiceRecords().size());
    }

    public Boolean checkFieldsBeforeSubmittingNewRepair() {
        if(getServiceName().trim().isEmpty() ||
                getTypeOfRepair().trim().isEmpty() ||
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

    // changes in table

    public void changeSPZCell(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        dataChanged = true;
        car.setSpz(carStringCellEditEvent.getNewValue());
    }

    public void changeMileAgeCell(TableColumn.CellEditEvent<Car, Integer> carStringCellEditEvent) {
        dataChanged = true;
        car.setMileage(carStringCellEditEvent.getNewValue());
    }

    public void changeEnginePowerCell(TableColumn.CellEditEvent<Car, Integer> carStringCellEditEvent) {
        dataChanged = true;
        car.setEngine_power(carStringCellEditEvent.getNewValue());
    }

    public void changePricePerDayCell(TableColumn.CellEditEvent<Car, Float> carStringCellEditEvent) {
        dataChanged = true;
        car.setPrice_per_day(carStringCellEditEvent.getNewValue());
    }

    @FXML
    void buttonAddNewServiceRecord(ActionEvent event) {

        if(checkFieldsBeforeSubmittingNewRepair() || !allServices.contains(getServiceName()) ||
            !allRepairs.contains(getTypeOfRepair())) {
            Notifications notification = Notifications.create()
                    .title("Nesprávne vyplnené údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        } else {

            CarManager carManager = new CarManager();

            if(!carManager.addNewServisToSpecificCar(car,null,getServiceName(),
                    getTypeOfRepair(),getDateOfService(),getPriceOfService())) {
                Notifications notification = Notifications.create()
                        .title("Nesprávny servis!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showError();
            } else {
                Notifications notification = Notifications.create()
                        .title("Záznam o servisovaní bol úspešne pridaný!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showConfirm();

                removeTypedInfo();

                tableServiceRecords.setItems(car.getServiceRecords());
                setNumberOfServices();
            }
        }
    }
}
