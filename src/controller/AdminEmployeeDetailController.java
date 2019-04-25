package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import model.Employee;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEmployeeDetailController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee, Integer> collumnID;
    @FXML private TableColumn<Employee, String> collumnFirstName;
    @FXML private TableColumn<Employee, String> collumnLastName;
    @FXML private TableColumn<Employee, String> collumnLogin;
    @FXML private TableColumn<Employee, String> collumnPassword;
    @FXML private TableColumn<Employee, String> collumnPhone;
    @FXML private TableColumn<Employee, String> collumnType;

    private Employee employee;

    private Boolean dataChanged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        collumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        collumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        collumnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        collumnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        collumnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        collumnType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.setEditable(true);
        collumnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnPhone.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        tableView.setItems(FXCollections.observableArrayList(employee));
    }

    public Boolean getDataChanged() {
        return dataChanged;
    }

    public void changeFirstNameCell(TableColumn.CellEditEvent<Employee, String> employeeStringCellEditEvent) {
        dataChanged = true;
        employee.setFirstName(employeeStringCellEditEvent.getNewValue());
    }

    public void changeLastNameCell(TableColumn.CellEditEvent<Employee, String> employeeStringCellEditEvent) {
        dataChanged = true;
        employee.setLastName(employeeStringCellEditEvent.getNewValue());
    }

    public void changePasswordCell(TableColumn.CellEditEvent<Employee, String> employeeStringCellEditEvent) {
        dataChanged = true;
        employee.setPassword(employeeStringCellEditEvent.getNewValue());
    }

    public void changePhoneCell(TableColumn.CellEditEvent<Employee, String> employeeStringCellEditEvent) {
        dataChanged = true;
        employee.setPhoneNumber(employeeStringCellEditEvent.getNewValue());
    }
}
