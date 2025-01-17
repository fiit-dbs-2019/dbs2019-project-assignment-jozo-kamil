package controller;

import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
import model.Employee;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeAddLegalPersonController implements Initializable {
    @FXML private AnchorPane rootPane;

    @FXML private JFXTextField textFieldIco;
    @FXML private JFXTextField textFieldDic;
    @FXML private JFXTextField textFieldName;
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

    public void btnAddPushed(ActionEvent actionEvent) throws SQLException,IOException {
        isTextEmpty();
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        backToMenu();
    }

    public boolean tooLongText(){
        if(getIco().length() > 8 ||
                getDic().length() > 10 ||
                getName().length()>255 ||
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
        else if (getIco().trim().isEmpty() || getAdress().trim().isEmpty() || getBankAccount().trim().isEmpty() || getDic().trim().isEmpty() || getPhone().trim().isEmpty() || getName().trim().isEmpty()) {
            Alert alertError = new Alert(Alert.AlertType.WARNING,"Vyplňte správne všetky údaje.", ButtonType.CLOSE);
            alertError.initStyle(StageStyle.TRANSPARENT);
            alertError.setHeaderText("Varovanie!");
            alertError.showAndWait();
        }
        else {
            PersonManager pm = new PersonManager();
            int result = pm.addNewLegalPersonToDatabase(getIco(),getDic(),getName(),getAdress(),getBankAccount(),getPhone());
            if(result==3){
                Alert alertOKInformation = new Alert(Alert.AlertType.INFORMATION,"Informácie o vypožičiavaťeľivi boli úspešne pridané.", ButtonType.CLOSE);
                alertOKInformation.initStyle(StageStyle.TRANSPARENT);
                alertOKInformation.setHeaderText("Info!");
                alertOKInformation.showAndWait();
//                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
//                rootPane.getChildren().setAll(pane);
                backToMenu();
            }
            else if (result == 0){
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Vypožičiavaťeľ s ID: "+getIco()+" sa už v systéme nachádza.", ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
            }
            else if (result ==1){
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Vypožičiavaťeľ s DIČ: "+getDic()+" sa už v systéme nachádza.", ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
            }
            else if (result==2){
                Alert alertError = new Alert(Alert.AlertType.ERROR,"Vypožičiavaťeľ s DIČ: "+getDic()+" a s IČO: "+getIco()+" sa už v systéme nachádza.", ButtonType.CLOSE);
                alertError.initStyle(StageStyle.TRANSPARENT);
                alertError.setHeaderText("Chyba!");
                alertError.showAndWait();
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
