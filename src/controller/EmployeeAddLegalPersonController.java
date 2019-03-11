package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
        PersonManager pm = new PersonManager();
        pm.addNewLegalPersonToDatabase(getIco(),getDic(),getName(),getAdress(),getBankAccount(),getPhone());

        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_customer.fxml"));
        rootPane.getChildren().setAll(pane);
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
