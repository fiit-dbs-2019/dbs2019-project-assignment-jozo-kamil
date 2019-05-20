package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Employee;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.textfield.TextFields;
import persistancemanagers.CarManager;
import persistancemanagers.EnumManager;
import view.PopUpWindow;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EmployeeAddCarController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private JFXComboBox comboBoxBrand;

    @FXML private JFXComboBox comboBoxModel;
    @FXML private Button buttonAddModel;

    @FXML private JFXDatePicker datePickerYear;

    @FXML private Spinner spinnerMileage;

    @FXML private JFXComboBox comboBoxFuel;

    @FXML private Spinner spinnerEngineCapacity;

    @FXML private Spinner spinnerEnginePower;

    @FXML private JFXComboBox comboBoxGearBox;

    @FXML private JFXComboBox comboBoxCarBody;

    @FXML private JFXComboBox comboBoxColor;
    @FXML private Button buttonAddColor;

    @FXML private JFXTextField textFieldSPZ;

    @FXML private JFXTextField textFieldVIN;

    @FXML private JFXTextField textFieldPrice;

    private Employee employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerConfig();
        addItemsComboBox();

        comboBoxModel.setDisable(true);
        buttonAddModel.setDisable(true);
        comboBoxCarBody.setDisable(true);
        comboBoxColor.setDisable(true);
        buttonAddColor.setDisable(true);
        comboBoxFuel.setDisable(true);
        comboBoxGearBox.setDisable(true);
        textFieldSPZ.setDisable(true);
        textFieldVIN.setDisable(true);
        textFieldPrice.setDisable(true);
        spinnerMileage.setDisable(true);
        spinnerEnginePower.setDisable(true);
        spinnerEngineCapacity.setDisable(true);
        datePickerYear.setDisable(true);
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
        spinnerEngineCapacity.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(2.5,5.0,1,0.1));
        spinnerEnginePower.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(80,500,180,1));
        spinnerMileage.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,10000,1000));
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Float getPrice() {
        return Float.parseFloat(textFieldPrice.getText());
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
        return textFieldSPZ.getText();
    }

    public String getVIN() {
        return textFieldVIN.getText();
    }

    public boolean tooLongText() {
        if(getSPZ().length() > 20 ||
                getVIN().length() > 17){
            return true;
        }
        return false;
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
        } else {
            try {
                Float.parseFloat(getPrice().toString());
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        backToMenu();
    }

    public void btnConfirmPushed(ActionEvent actionEvent) throws SQLException, IOException {

        if(tooLongText()) {
            Notifications notification = Notifications.create()
                    .title("Príliš dlhé údaje v kolonke ŠPZ/VIN! Max. počet znakov pre VIN je 17, pre ŠPZ 20.")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showWarning();
            return;
        }

        if(emptyFieldChecker()) {

            Notifications notification = Notifications.create()
                    .title("Vypíšte správne všetky údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showWarning();
            return;
        }

        if(!getYear().before(Calendar.getInstance().getTime())){

            Notifications notification = Notifications.create()
                    .title("Rok výroby auta je neplatný!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
            return;
        } else {
            CarManager cm = new CarManager();
            if(cm.addNewCarToDatabase(getVIN(),getBrand(),getModel(),getCarBody(),getEngineCapacity(),getEnginePower(),getGearBox(),getFuel(),getColor(),
                    getPrice(),getYear(),getMileAge(),getSPZ())) {

                Notifications notification = Notifications.create()
                        .title("Auto s VIN číslom " + getVIN() + " bolo pridané!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showConfirm();

                    backToMenu();
            } else {

                Notifications notification = Notifications.create()
                        .title("Záznam o aute s VIN číslom " + getVIN() + " už existuje!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showError();
            }
        }
    }

    public void backToMenu() {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_menu.fxml"));
            parent = (Parent) loaader.load();

            EmployeeMenuController employeeMenuController = loaader.getController();
            employeeMenuController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void comboBoxModelClicked(MouseEvent mouseEvent) {

        if(getBrand() == null) {
            return;
        } else {
            EnumManager em = new EnumManager();
            try {
                em.setModelsForSpecificBrand(getBrand(),comboBoxModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddBrandPushed(ActionEvent actionEvent) throws IOException {
        PopUpWindow popUpWindow = new PopUpWindow();
        String newItem = popUpWindow.display(null,"brand","Zadaj značku: ");

        if(newItem != null) {
            try {
                EnumManager em = new EnumManager();

                em.employeeTypeEnum(comboBoxBrand, "car_brand");
                comboBoxBrand.setEditable(true);
                TextFields.bindAutoCompletion(comboBoxBrand.getEditor(),comboBoxBrand.getItems());

                comboBoxBrand.setValue(newItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddModelPushed(ActionEvent actionEvent) {
        if(getBrand() == null) {
            Notifications notification = Notifications.create()
                    .title("Pre pridanie modelu vyber príslušnú značku!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
            return;
        }

        PopUpWindow popUpWindow = new PopUpWindow();
        String newItem = popUpWindow.display(getBrand(),"model","Zadaj model: ");

        if(newItem != null) {
            try {
                EnumManager em = new EnumManager();

                em.employeeTypeEnum(comboBoxModel, "car_model");

                comboBoxModel.setValue(newItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddColorPushed(ActionEvent actionEvent) {
        PopUpWindow popUpWindow = new PopUpWindow();

        String newItem = popUpWindow.display(null,"color","Zadaj farbu: ");

        if(newItem != null) {
            try {
                EnumManager em = new EnumManager();

                em.employeeTypeEnum(comboBoxColor,"car_color");

                comboBoxColor.setValue(newItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void comboBoxBrandSelected(ActionEvent actionEvent) {
        comboBoxModel.setDisable(false);
        buttonAddModel.setDisable(false);
    }

    public void comboBoxModelSelected(ActionEvent actionEvent) {
        datePickerYear.setDisable(false);
    }

    public void datePickerDateSelected(ActionEvent actionEvent) {
        spinnerMileage.setDisable(false);
        comboBoxFuel.setDisable(false);
    }

    public void comboBoxFuelSelected(ActionEvent actionEvent) {
        spinnerEnginePower.setDisable(false);
        spinnerEngineCapacity.setDisable(false);
        comboBoxGearBox.setDisable(false);
    }

    public void comboBoxGearBoxSelected(ActionEvent actionEvent) {
        comboBoxCarBody.setDisable(false);
    }

    public void comboBoxCarBodySelected(ActionEvent actionEvent) {
        comboBoxColor.setDisable(false);
        buttonAddColor.setDisable(false);
    }

    public void comboBoxColorSelected(ActionEvent actionEvent) {
        textFieldSPZ.setDisable(false);
        textFieldVIN.setDisable(false);
        textFieldPrice.setDisable(false);
    }
}
