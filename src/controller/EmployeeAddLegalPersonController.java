package controller;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
import java.util.ResourceBundle;

public class EmployeeAddLegalPersonController implements Initializable {
    @FXML private TextField textFieldIco;
    @FXML private TextField textFieldDic;
    @FXML private TextField textFieldName;
    @FXML private AnchorPane rootPane;
    @FXML private TextField textFieldAdress;
    @FXML private TextField textFieldBankAccount;
    @FXML private TextField textFieldPhone;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnAddPushed(ActionEvent actionEvent) throws SQLException,IOException {
        isTextEmpty();
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_customer.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void isTextEmpty() throws SQLException,IOException{
        if (getIco().trim().isEmpty() || getAdress().trim().isEmpty() || getBankAccount().trim().isEmpty() || getDic().trim().isEmpty() || getPhone().trim().isEmpty() || getName().trim().isEmpty()) {
            Alert alertError = new Alert(Alert.AlertType.WARNING,"Vyplňte správne všetky údaje.", ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Varovanie!");
            alertError.showAndWait();
        }
        else {
            PersonManager pm = new PersonManager();

            if(pm.addNewLegalPersonToDatabase(getIco(),getDic(),getName(),getAdress(),getBankAccount(),getPhone())){
                Alert alertOKInformation = new Alert(Alert.AlertType.INFORMATION,"Informácie o žiadateľovi boli úspešne pridané.", ButtonType.CLOSE);
                alertOKInformation.initStyle(StageStyle.TRANSPARENT);
                alertOKInformation.setHeaderText("Info!");
                alertOKInformation.showAndWait();
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
                rootPane.getChildren().setAll(pane);
            }
            else {
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Žiadateteľ s IČO: "+getIco()+" sa už v systéme nachádza.", ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
            }

        }
    }

    public String getIco(){
        return textFieldIco.getText();
    }

    public String getDic(){
        return textFieldDic.getText();
    }

    public String getName(){
        return textFieldName.getText();
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
