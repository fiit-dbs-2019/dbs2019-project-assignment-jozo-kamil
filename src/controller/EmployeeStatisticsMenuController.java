package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Employee;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EmployeeStatisticsMenuController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelDate;
    @FXML private Label labelLastName;

    private Employee employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setHeader () {
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setHeader();
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

    public void btnBackPushed(ActionEvent actionEvent) {
        backToMenu();
    }

    public void btnCarFromContractStatisticPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_carFromContract_statistic.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCarFromContractStatisticController employeeCarFromContractStatisticController = loaader.getController();
            employeeCarFromContractStatisticController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnCarFromCarStatisticPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_carFromCar_statistic.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCarFromCarStatisticController employeeCarFromCarStatisticController = loaader.getController();
            employeeCarFromCarStatisticController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnEmployeeFromContractStatisticPushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_employeeFromContract_statistic.fxml"));
            parent = (Parent) loaader.load();

            EmployeeEmployeeFromContractStatisticController employeeEmployeeFromContractStatisticController = loaader.getController();
            employeeEmployeeFromContractStatisticController.setEmployee(employee);

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
