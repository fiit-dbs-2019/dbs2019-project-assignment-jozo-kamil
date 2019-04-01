package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;
import org.controlsfx.control.textfield.TextFields;
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
    @FXML private JFXTextField textFieldFirstName;
    @FXML private JFXTextField textFieldLastName;
    @FXML private JFXTextField textFieldLogin;
    @FXML private JFXTextField textFieldPassword;
    @FXML private JFXTextField textPhoneNumber;
    @FXML private JFXComboBox comboBoxType;

    private Employee admin;

    public void setHeader () {
//        EmployeeManager em = new EmployeeManager();
//
//        try {
//            em.setHeader(labelFirstName,labelLastName);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        labelFirstName.setText(admin.getFirstName());
        labelLastName.setText(admin.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setHeader();
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

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
        setHeader();
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

    public boolean tooLongText(){
        if (getFirstName().length() > 255 ||
                getLastName().length() > 255 ||
                getLogin().length() > 255 ||
                getPassword().length() > 255 ||
                getPhone().length() > 255) {
            return true;
        }
        return false;
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
        if (tooLongText()){
            Alert alertEmptyField = new Alert(Alert.AlertType.WARNING,"Príliš dlhé údaje. Max. veľkosť jedného údaju je 254 znakov!", ButtonType.CLOSE);
            alertEmptyField.initStyle(StageStyle.TRANSPARENT);
            alertEmptyField.setHeaderText("Varovanie!");
            alertEmptyField.showAndWait();
            return;
        }
        else if(emptyFieldChecker()){
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

//                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
//                rootPane.getChildren().setAll(pane);
                backToMenu();

            } else {
                Alert alertLoginAlreadyExist = new Alert(Alert.AlertType.ERROR,"Zadaný login - " + getLogin() + " - už existuje!", ButtonType.CLOSE);
                alertLoginAlreadyExist.initStyle(StageStyle.TRANSPARENT);
                alertLoginAlreadyExist.setHeaderText("Chyba!");
                alertLoginAlreadyExist.showAndWait();
            }
        }
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        backToMenu();
    }

    public void backToMenu() {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/admin_menu.fxml"));
            parent = (Parent) loaader.load();

            AdminMenuController adminMenuController = loaader.getController();
            adminMenuController.setAdmin(admin);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }
}
