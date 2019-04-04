package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import model.NaturalPerson;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeNaturalPersonDetailController implements Initializable {

    @FXML private TableColumn<NaturalPerson, String> collumnID;
    @FXML private AnchorPane rootPane;
    @FXML private TableView<NaturalPerson> tableView;
    @FXML private TableColumn<NaturalPerson, String> collumnFirstName;
    @FXML private TableColumn<NaturalPerson, String> collumnLastName;
    @FXML private TableColumn<NaturalPerson, String> collumnAdress;
    @FXML private TableColumn<NaturalPerson, String> collumnBankAccount;
    @FXML private TableColumn<NaturalPerson, String> collumnPhone;

    private NaturalPerson naturalPerson;
    private Boolean dataChanged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        collumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        collumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        collumnAdress.setCellValueFactory(new PropertyValueFactory<>("adress"));
        collumnBankAccount.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));
        collumnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        tableView.setEditable(true);
        collumnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnAdress.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnBankAccount.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnPhone.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
        tableView.setItems(FXCollections.observableArrayList(naturalPerson));
    }

    public Boolean getDataChanged() {
        return dataChanged;
    }

    public void changeFirstNameCell(TableColumn.CellEditEvent<NaturalPerson, String> naturalPersonStringCellEditEvent) {
        dataChanged = true;
        naturalPerson.setFirstName(naturalPersonStringCellEditEvent.getNewValue());
    }

    public void changeLastNameCell(TableColumn.CellEditEvent<NaturalPerson, String> naturalPersonStringCellEditEvent) {
        dataChanged = true;
        naturalPerson.setLastName(naturalPersonStringCellEditEvent.getNewValue());
    }

    public void changeAdressCell(TableColumn.CellEditEvent<NaturalPerson, String> naturalPersonStringCellEditEvent) {
        dataChanged = true;
        naturalPerson.setAdress(naturalPersonStringCellEditEvent.getNewValue());
    }

    public void changeBankAccountCell(TableColumn.CellEditEvent<NaturalPerson, String> naturalPersonStringCellEditEvent) {
        dataChanged = true;
        naturalPerson.setBankAccount(naturalPersonStringCellEditEvent.getNewValue());
    }

    public void changePhoneCell(TableColumn.CellEditEvent<NaturalPerson, String> naturalPersonStringCellEditEvent) {
        dataChanged = true;
        naturalPerson.setPhoneNumber(naturalPersonStringCellEditEvent.getNewValue());
    }
}
