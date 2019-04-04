package controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Car;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
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

    private Car car;

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
    }

    public void setCar(Car car) {
        this.car = car;
        tableNonEditable.setItems(FXCollections.observableArrayList(car));
        tableEditable.setItems(FXCollections.observableArrayList(car));
    }

    public Boolean getDataChanged() {
        return dataChanged;
    }


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

    public void changePricePerDayCell(TableColumn.CellEditEvent<Car, Integer> carStringCellEditEvent) {
        dataChanged = true;
        car.setEngine_power(carStringCellEditEvent.getNewValue());
    }
}
