package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Employee;
import org.controlsfx.control.Notifications;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeAddNaturalPersonController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private JFXTextField textFieldFirstName;
    @FXML private JFXTextField textFieldLastName;
    @FXML private JFXTextField textFieldID;
    @FXML private JFXTextField textFieldAdress;
    @FXML private JFXTextField textFieldBankAccount;
    @FXML private JFXTextField textFieldPhone;

    private Employee employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
         backToMenu();
    }

    public void btnAddPushed(ActionEvent actionEvent) throws SQLException,IOException{
        isTextEmpty();
    }

    public boolean tooLongText(){
        if(getLastName().length() > 255 ||
                getID().length() > 8 ||
                getFirstName().length()>255 ||
                getAdress().length()>255 ||
                getBankAccount().length()>30 ||
                getPhone().length()>255){
            return true;
        }
        return false;
    }

    public void isTextEmpty() throws SQLException,IOException{
        if (tooLongText()) {
//            Alert alertError = new Alert(Alert.AlertType.WARNING,"Príliš dlhé údaje, niektoré položky majú obmedzený počet znakov, prosím skontrolujte, či ste všetky informácie zadali správne!", ButtonType.CLOSE);
//            alertError.initStyle(StageStyle.TRANSPARENT);
//            alertError.setHeaderText("Varovanie!");
//            alertError.showAndWait();
            Notifications notification = Notifications.create()
                    .title("Príliš dlhé údaje, niektoré položky majú obmedzený počet znakov, prosím skontrolujte, či ste všetky informácie zadali správne!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        }
        else if (getFirstName().trim().isEmpty() || getAdress().trim().isEmpty() || getBankAccount().trim().isEmpty() || getLastName().trim().isEmpty() || getPhone().trim().isEmpty() || getID().trim().isEmpty()) {
//            Alert alertError = new Alert(Alert.AlertType.WARNING,"Vyplňte správne všetky údaje.", ButtonType.CLOSE);
//            alertError.initStyle(StageStyle.TRANSPARENT);
//            alertError.setHeaderText("Varovanie!");
//            alertError.showAndWait();
            Notifications notification = Notifications.create()
                    .title("Vyplňte správne všetky údaje!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showWarning();
        }
        else {
            PersonManager pm = new PersonManager();

            if (pm.addNewNaturalPersonToDatabase(getID(),getFirstName(),getLastName(),getAdress(),getBankAccount(),getPhone())){

//                Alert alertOKInformation = new Alert(Alert.AlertType.INFORMATION,"Informácie o vypožičiavaťeľovi boli úspešne pridané.", ButtonType.CLOSE);
//                alertOKInformation.initStyle(StageStyle.TRANSPARENT);
//                alertOKInformation.setHeaderText("Info!");
//                alertOKInformation.showAndWait();
                Notifications notification = Notifications.create()
                        .title("Informácie o vypožičiavaťeľovi boli úspešne pridané!")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showConfirm();

//                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
//                rootPane.getChildren().setAll(pane);
                backToMenu();
            }
            else {
//                Alert alertError = new Alert(Alert.AlertType.ERROR,"Vypožičiavaťeľ s ID: "+getID()+" sa už v systéme nachádza.", ButtonType.CLOSE);
//                alertError.initStyle(StageStyle.TRANSPARENT);
//                alertError.setHeaderText("Chyba!");
//                alertError.showAndWait();
                Notifications notification = Notifications.create()
                        .title("Vypožičiavaťeľ s ID: "+getID()+" sa už v systéme nachádza!")
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

    public String getFirstName(){
        return textFieldFirstName.getText();
    }

    public String getLastName() {
        return textFieldLastName.getText();
    }

    public String getID(){
        return textFieldID.getText();
    }

    public String getAdress(){
        return textFieldAdress.getText();
    }

    public String getBankAccount(){
        return textFieldBankAccount.getText();
    }

    public String getPhone(){
        return textFieldPhone.getText();
    }
}
