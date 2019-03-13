package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import model.Employee;
import persistancemanagers.EmployeeManager;
import persistancemanagers.EnumManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AdminAddEmployeeController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;
    @FXML private TextField textFieldFirstName;
    @FXML private TextField textFieldLastName;
    @FXML private TextField textFieldLogin;
    @FXML private TextField textFieldPassword;
    @FXML private TextField textPhoneNumber;
    @FXML private ComboBox comboBoxType;

    public void setHeader () {
        EmployeeManager em = new EmployeeManager();

        try {
            em.setHeader(labelFirstName,labelLastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setHeader();
        addItemsComboBox();
    }

    public void addItemsComboBox() {
        try {
            EnumManager em = new EnumManager();

            em.employeeTypeEnum(comboBoxType,"employee_type");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFirstName() {
        return textFieldFirstName.getText();
    }

    public String getLastName() {
        return textFieldLastName.getText();
    }

    public String getLogin() {
        return textFieldLogin.getText();
    }

    public String getPassword() {
        return textFieldPassword.getText();
    }

    public String getPhone() { return textPhoneNumber.getText(); }

    public String getType() {
        if(comboBoxType.getSelectionModel().isEmpty()) {
            return null;
        }
        return comboBoxType.getSelectionModel().getSelectedItem().toString();
    }

    public boolean emptyFieldChecker() {
        if (getFirstName().trim().isEmpty() ||
            getLastName().trim().isEmpty() ||
            getLogin().trim().isEmpty() ||
            getPassword().trim().isEmpty() ||
            getPhone().trim().isEmpty() ||
            getType() == null) {
            return true;
        }
        return false;
    }

    public void btnAddEmployeePushed(ActionEvent actionEvent) throws SQLException, IOException {
        if(emptyFieldChecker()){
            Alert alertEmptyField = new Alert(Alert.AlertType.WARNING,"Vypíšte správne všetky údaje!", ButtonType.CLOSE);
            alertEmptyField.initStyle(StageStyle.TRANSPARENT);
            alertEmptyField.setHeaderText("Varovanie!");
            alertEmptyField.showAndWait();
            return;
        } else {

            EmployeeManager em = new EmployeeManager();

            if(em.AddNewEmployeeToDatabase(getFirstName(),getLastName(),getLogin(),getPassword(), getPhone(), getType())){

                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION,"Konto zamestnanca bolo úspešne vytvorené!", ButtonType.CLOSE);
                alertInfo.initStyle(StageStyle.TRANSPARENT);
                alertInfo.setHeaderText("Info!");
                alertInfo.showAndWait();

                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
                rootPane.getChildren().setAll(pane);
            } else {
                Alert alertLoginAlreadyExist = new Alert(Alert.AlertType.ERROR,"Zadaný login - " + getLogin() + " - už existuje!", ButtonType.CLOSE);
                alertLoginAlreadyExist.initStyle(StageStyle.TRANSPARENT);
                alertLoginAlreadyExist.setHeaderText("Chyba!");
                alertLoginAlreadyExist.showAndWait();
            }
        }
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
