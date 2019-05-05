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
import javafx.util.Duration;
import model.Employee;
import org.controlsfx.control.Notifications;
import persistancemanagers.EmployeeManager;
import persistancemanagers.EnumManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
        labelFirstName.setText(admin.getFirstName());
        labelLastName.setText(admin.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addItemsComboBox();
    }

    public void addItemsComboBox() {
        EnumManager em = new EnumManager();

        em.employeeTypeEnum(comboBoxType,"employee_type");
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
            Notifications notification = Notifications.create()
                    .title("Príliš dlhé údaje. Max. veľkosť jedného údaju je 254 znakov!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
            return;
        }
        else if(emptyFieldChecker()){
            Notifications notification = Notifications.create()
                    .title("Vypíšte správne všetky údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
            return;
        } else {

            EmployeeManager em = new EmployeeManager();

            if(em.addNewEmployee(getFirstName(),getLastName(),getLogin(),getPassword(), getPhone(), getType())){

                Notifications notification = Notifications.create()
                        .title("Konto zamestnanca bolo úspešne vytvorené!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showConfirm();

                backToMenu();

            } else {

                Notifications notification = Notifications.create()
                        .title("Zadaný login - " + getLogin() + " - už existuje!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showError();
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
