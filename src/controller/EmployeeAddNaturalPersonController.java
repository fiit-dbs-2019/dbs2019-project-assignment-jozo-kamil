package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeAddNaturalPersonController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private TextField textFieldFirstName;
    @FXML private TextField textFieldLastName;
    @FXML private TextField textFieldID;
    @FXML private TextField textFieldAdress;
    @FXML private TextField textFieldBankAccount;
    @FXML private TextField textFieldPhone;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_customer.fxml"));
        rootPane.getChildren().setAll(pane);
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
            Alert alertError = new Alert(Alert.AlertType.WARNING,"Príliš dlhé údaje, niektoré položky majú obmedzený počet znakov, prosím skontrolujte, či ste všetky informácie zadali správne!", ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Varovanie!");
            alertError.showAndWait();
        }
        else if (getFirstName().trim().isEmpty() || getAdress().trim().isEmpty() || getBankAccount().trim().isEmpty() || getLastName().trim().isEmpty() || getPhone().trim().isEmpty() || getID().trim().isEmpty()) {
            Alert alertError = new Alert(Alert.AlertType.WARNING,"Vyplňte správne všetky údaje.", ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Varovanie!");
            alertError.showAndWait();
        }
        else {
            PersonManager pm = new PersonManager();

            if (pm.addNewNaturalPersonToDatabase(getID(),getFirstName(),getLastName(),getAdress(),getBankAccount(),getPhone())){

                Alert alertOKInformation = new Alert(Alert.AlertType.INFORMATION,"Informácie o žiadateľovi boli úspešne pridané.", ButtonType.CLOSE);
                alertOKInformation.initStyle(StageStyle.TRANSPARENT);
                alertOKInformation.setHeaderText("Info!");
                alertOKInformation.showAndWait();

                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
                rootPane.getChildren().setAll(pane);
            }
            else {
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Žiadateteľ s číslom OP: "+getID()+" sa už v systéme nachádza.", ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
            }
        }
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
