package controller;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.util.Duration;
import model.Employee;
import org.controlsfx.control.Notifications;
import persistancemanagers.AllTablesManager;
import persistancemanagers.CreateContractManager;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmployeeCreateContractController implements Initializable {
    @FXML private AnchorPane rootPane;

    @FXML private JFXDatePicker datePickerFrom;
    @FXML private JFXDatePicker datePickerTo;

    @FXML private JFXTextField textFieldID;
    @FXML private JFXTextField textFieldVIN;

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

    public void btnCreateContractPushed(ActionEvent actionEvent) throws SQLException,IOException {

       if(getTextFieldOp().trim().isEmpty() || getTextFieldVin().trim().isEmpty()) {
//            Alert alertError = new Alert(Alert.AlertType.WARNING,"Zadajte údaje.",ButtonType.CLOSE);
//            alertError.initStyle(StageStyle.TRANSPARENT);
//            alertError.setHeaderText("Varovanie!");
//            alertError.showAndWait();
           Notifications notification = Notifications.create()
                   .title("Zadajte údaje!")
                   .hideAfter(Duration.seconds(4))
                   .hideCloseButton();
           notification.showWarning();
        }else if (getDateTo() == null || getDateFrom() == null || getDateTo().before(getDateFrom())) {
//            Alert alertError = new Alert(Alert.AlertType.WARNING,"Zadajte správne dobu trvania.",ButtonType.CLOSE);
//            alertError.initStyle(StageStyle.TRANSPARENT);
//            alertError.setHeaderText("Varovanie!");
//            alertError.showAndWait();
           Notifications notification = Notifications.create()
                   .title("Zadajte správne dobu trvania!")
                   .hideAfter(Duration.seconds(4))
                   .hideCloseButton();
           notification.showWarning();
        } else {
                CreateContractManager ccm = new CreateContractManager();

                int result = ccm.checkInfo(getTextFieldVin(),getTextFieldOp(),getDateFrom(),getDateTo());

                if (result == 0) {
//                    Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané VIN číslo: "+getTextFieldVin()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
//                    alertError.initStyle(StageStyle.TRANSPARENT);
//                    alertError.setHeaderText("Chyba!");
//                    alertError.showAndWait();
                    Notifications notification = Notifications.create()
                            .title("Zadané VIN číslo: "+getTextFieldVin()+ " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                } else if (result ==1) {
//                    Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané OP číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
//                    alertError.initStyle(StageStyle.TRANSPARENT);
//                    alertError.setHeaderText("Chyba!");
//                    alertError.showAndWait();
                    Notifications notification = Notifications.create()
                            .title("Zadané ID číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                } else if (result ==2){
//                    Alert alertError = new Alert(Alert.AlertType.ERROR,"Zadané VIN číslo: "+getTextFieldVin()+" a OP číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje.",ButtonType.CLOSE);
//                    alertError.initStyle(StageStyle.TRANSPARENT);
//                    alertError.setHeaderText("Chyba!");
//                    alertError.showAndWait();
                    Notifications notification = Notifications.create()
                            .title("Zadané VIN číslo: "+getTextFieldVin()+" a ID číslo žiadateľa: "+getTextFieldOp()+ " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                } else if (result ==3 ){

//                    Properties prop = new Properties();
//                    OutputStream output = null;
//
//                    FileInputStream input = new FileInputStream("src/contract_info");
//                    prop.load(input);
//
////                    input = new FileInputStream("src/contract_info");
////                    prop.load(input);
//
//                    try {
//                        output = new FileOutputStream("src/contract_info");
//
//                        prop.setProperty("carVIN",getTextFieldVin());
//                        prop.setProperty("customerID",getTextFieldOp());
//                        prop.setProperty("dateFROM",getDateFrom().toString());
//                        prop.setProperty("dateTO",getDateTo().toString());
//
//                        prop.store(output, null);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (output != null) {
//                            try {
//                                output.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }

//                    AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_confirm_contract.fxml"));
//                    rootPane.getChildren().setAll(pane);

                    Parent parent = null;
                    try {
                        FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_confirm_contract.fxml"));
                        parent = (Parent) loaader.load();

                        EmployeeConfirmContractController employeeConfirmContractController = loaader.getController();
                        employeeConfirmContractController.setEmployee(employee);
                        employeeConfirmContractController.setCustomerID(getTextFieldOp());
                        employeeConfirmContractController.setCarVIN(getTextFieldVin());
                        employeeConfirmContractController.setDateFROM(getDateFrom());
                        employeeConfirmContractController.setDateTO(getDateTo());
                        employeeConfirmContractController.setAll();

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

    }

    public void btnBackPushed(ActionEvent actionEvent) {
        backToMenu();
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

    public String getTextFieldOp() {
        return textFieldID.getText();
    }

    public String getTextFieldVin() {
        return textFieldVIN.getText();
    }

    public Date getDateFrom() {
        if (datePickerFrom.getValue()==null){
            return null;
        }
        Date date = Date.valueOf(datePickerFrom.getValue());

        return date;
    }

    public Date getDateTo() {
        if (datePickerTo.getValue()==null){
            return null;
        }
        Date date = Date.valueOf(datePickerTo.getValue());

        return date;
    }
}
