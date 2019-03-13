package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import persistancemanagers.CarManager;
import persistancemanagers.EnumManager;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
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
        try {
            EnumManager em = new EnumManager();

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
        if(comboBoxBrand.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxBrand.getSelectionModel().getSelectedItem().toString();
    }

    public String getModel() {
        if(comboBoxModel.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxModel.getSelectionModel().getSelectedItem().toString();
    }

    public Integer getMileAge() {
        return Integer.parseInt(spinnerMileage.getValue().toString());
    }

    public String getFuel() {
        if(comboBoxFuel.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxFuel.getSelectionModel().getSelectedItem().toString();
    }

    public String getGearBox() {
        if(comboBoxGearBox.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxGearBox.getSelectionModel().getSelectedItem().toString();
    }

    public String getCarBody() {
        if(comboBoxCarBody.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxCarBody.getSelectionModel().getSelectedItem().toString();
    }

    public String getColor() {
        if(comboBoxColor.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxColor.getSelectionModel().getSelectedItem().toString();
    }

    public Integer getPrice() {
        return Integer.parseInt(spinnerPrice.getValue().toString());
    }

    public Date getYear() {
        if(datePickerYear.getValue() == null) {
            return null;
        }
        Date date = Date.valueOf(datePickerYear.getValue());
        return date;
    }

    public Float getEngineCapacity() {
        return Float.parseFloat(spinnerEngineCapacity.getValue().toString());
    }

    public Integer getEnginePower() {
        return Integer.parseInt(spinnerEnginePower.getValue().toString());
    }

    public String getSPZ() {
        return textFieldSpz.getText();
    }

    public String getVIN() {
        return textFieldVin.getText();
    }

    public boolean emptyFieldChecker() {
        if(getBrand() == null ||
            getModel() == null ||
            getFuel() == null ||
            getGearBox() == null ||
            getCarBody() == null ||
            getColor() == null ||
            getYear() == null ||
            getSPZ().trim().isEmpty() ||
            getVIN().trim().isEmpty()){
            return true;
        }
        return false;
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnConfirmPushed(ActionEvent actionEvent) throws SQLException, IOException {

        if(emptyFieldChecker()) {
            Alert alertEmptyField = new Alert(Alert.AlertType.WARNING, "Vypíšte správne všetky údaje!", ButtonType.CLOSE);
            alertEmptyField.initStyle(StageStyle.TRANSPARENT);
            alertEmptyField.setHeaderText("Varovanie!");
            alertEmptyField.showAndWait();
            return;
        }

        if(!getYear().before(Calendar.getInstance().getTime())){
            Alert alertBadDate = new Alert(Alert.AlertType.ERROR,"Rok výroby auta je neplatný!", ButtonType.CLOSE);
            alertBadDate.initStyle(StageStyle.TRANSPARENT);
            alertBadDate.setHeaderText("Chyba!");
            alertBadDate.showAndWait();

            return;
        } else {
            CarManager cm = new CarManager();
            if(cm.addNewCarToDatabase(getVIN(),getBrand(),getModel(),getCarBody(),getEngineCapacity(),getEnginePower(),getGearBox(),getFuel(),getColor(),
                    getPrice(),getYear(),getMileAge(),getSPZ())) {

                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION,"Auto s VIN číslom " + getVIN() + " bolo pridané!", ButtonType.CLOSE);
                alertInfo.initStyle(StageStyle.TRANSPARENT);
                alertInfo.setHeaderText("Info!");
                alertInfo.showAndWait();

                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
                rootPane.getChildren().setAll(pane);

            } else {
                Alert alertLoginAlreadyExist = new Alert(Alert.AlertType.ERROR,"Záznam o aute s VIN číslom " + getVIN() + " už existuje!", ButtonType.CLOSE);
                alertLoginAlreadyExist.initStyle(StageStyle.TRANSPARENT);
                alertLoginAlreadyExist.setHeaderText("Chyba!");
                alertLoginAlreadyExist.showAndWait();
            }
        }
    }

    public void comboBoxModelClicked(MouseEvent mouseEvent) throws SQLException {

        if(getBrand() == null) {
            return;
        } else {
            EnumManager em = new EnumManager();
            em.setModelsForSpecificBrand(getBrand(),comboBoxModel);
        }
    }

}
