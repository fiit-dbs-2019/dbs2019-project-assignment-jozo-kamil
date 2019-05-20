package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import model.Car;
import model.Employee;
import model.Person;
import org.controlsfx.control.Notifications;
import persistancemanagers.AllTablesManager;
import persistancemanagers.CreateContractManager;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class EmployeeCreateContractController implements Initializable {


    @FXML private AnchorPane rootPane;

    @FXML private JFXTextField textFieldID;
    @FXML private JFXTextField textFieldVIN;

    @FXML private JFXDatePicker datePickerFrom;
    @FXML private JFXDatePicker datePickerTo;

    @FXML private JFXProgressBar progressBar;

    // logged employee
    private Employee employee;

    // [optional] - car from searching
    private Car car = null;

    // [optional\ - person from searching
    private Person customer = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressBar.setVisible(false);
    }

    // SETTERS AND GETTERS FOR ENTITY

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCar(Car car) {
        this.car = car;
        setCarVin();
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
        setCustomerID();
    }

    public void setCarVin() {
        textFieldVIN.setText(car.getCar_vin());
    }

    public void setCustomerID() {
        textFieldID.setText(customer.getID());
    }

    public void setSelectedCarVIN(String VIN) {
        textFieldVIN.setText(VIN);
    }

    public void setSelectedCustomerID(String ID) {
        textFieldID.setText(ID);
    }

    // SETTERS AND GETTERS FOR LABELS

    public String getTextFieldID() {
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

    // BUTTONS EVENTS

    public void btnCreateContractPushed(ActionEvent actionEvent) throws SQLException,IOException {
        Parent parent;

       if(getTextFieldID().trim().isEmpty() || getTextFieldVin().trim().isEmpty()) {

           Notifications notification = Notifications.create()
                   .title("Zadajte údaje!")
                   .hideAfter(Duration.seconds(4))
                   .hideCloseButton();
           notification.showWarning();

        }else if (getDateTo() == null || getDateFrom() == null || getDateTo().before(getDateFrom())) {

           Notifications notification = Notifications.create()
                   .title("Zadajte správne dobu trvania!")
                   .hideAfter(Duration.seconds(4))
                   .hideCloseButton();
           notification.showWarning();

        } else {
                CreateContractManager ccm = new CreateContractManager();

                int result = ccm.checkInfo(getTextFieldVin(),getTextFieldID(),getDateFrom(),getDateTo());

                if (result == 0) {

                    Notifications notification = Notifications.create()
                            .title("Zadané VIN číslo: "+getTextFieldVin()+ " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();

                } else if (result ==1) {

                    Notifications notification = Notifications.create()
                            .title("Zadané ID číslo žiadateľa: " + getTextFieldID() + " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();

                } else if (result ==2){

                    Notifications notification = Notifications.create()
                            .title("Zadané VIN číslo: "+getTextFieldVin()+" a ID číslo žiadateľa: "+getTextFieldID()+ " sa v databáze nevyskytuje!")
                            .hideAfter(Duration.seconds(4))
                            .hideCloseButton();
                    notification.showError();
                } else if (result ==3 ){

                    //Parent parent = null;
                    try {
                        FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_confirm_contract.fxml"));
                        parent = (Parent) loaader.load();

                        EmployeeConfirmContractController employeeConfirmContractController = loaader.getController();
                        employeeConfirmContractController.setEmployee(employee);

                        if(car != null) {
                            employeeConfirmContractController.setCar(car);
                        } else {
                            employeeConfirmContractController.setCarVIN(getTextFieldVin());
                        }

                        if(customer != null) {
                            employeeConfirmContractController.setCustomer(customer);
                        } else {
                            employeeConfirmContractController.setCustomerID(getTextFieldID());
                        }

                        employeeConfirmContractController.setDateFROM(getDateFrom());
                        employeeConfirmContractController.setDateTO(getDateTo());

                        Task setLabels = new Task() {
                            @Override
                            protected Object call() throws Exception {

                                progressBar.setVisible(true);

                                employeeConfirmContractController.setCarAndCustomer();

                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            employeeConfirmContractController.setAllLabels();
                                        } finally {
                                            latch.countDown();
                                        }
                                    }
                                });
                                latch.await();

                                return null;
                            }
                        };

                        setLabels.setOnSucceeded(event -> {
                            progressBar.setVisible(false);

                            Scene newScene = new Scene(parent);

                            //This line gets the Stage information
                            Stage currentStage = (Stage) rootPane.getScene().getWindow();

                            currentStage.setScene(newScene);
                            currentStage.show();
                        });

                        Thread thread = new Thread(setLabels);
                        thread.setDaemon(true);
                        thread.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void buttonSearchCustomerPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_search_customer.fxml"));
            parent = (Parent) loaader.load();

            EmployeeSearchCustomerController employeeSearchCustomerController = loaader.getController();
            employeeSearchCustomerController.setEmployee(employee);
            employeeSearchCustomerController.setOpenedFromContractScene(true);

            if(car != null) {
                employeeSearchCustomerController.setCar(car);
            } else if (!getTextFieldVin().trim().isEmpty()) {
                employeeSearchCustomerController.setCarVIN(getTextFieldVin());
            }

            employeeSearchCustomerController.addItemsToListNatural();
            employeeSearchCustomerController.addItemsToTableNatural();

            employeeSearchCustomerController.addItemsToListLegal();
            employeeSearchCustomerController.addItemsToTableLegal();

            employeeSearchCustomerController.setNewRangeOfDisplayedDataNatural();
            employeeSearchCustomerController.setNewRangeOfDisplayedDataLegal();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void buttonSearchCarPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_search_car.fxml"));
            parent = (Parent) loader.load();

            EmployeeSearchCarController employeeSearchCarController = loader.getController();
            employeeSearchCarController.setEmployee(employee);
            employeeSearchCarController.setOpenedFromContractScene(true);

            if(customer != null) {
                employeeSearchCarController.setCustomer(customer);
            } else if (!getTextFieldID().trim().isEmpty()) {
                employeeSearchCarController.setCustomerID(getTextFieldID());
            }

            employeeSearchCarController.addItemsToList();
            employeeSearchCarController.addItemsToTable();

            employeeSearchCarController.setNewRangeOfDisplayedData();

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
