package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import model.LegalPerson;
import model.NaturalPerson;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeLegalPersonDetailController implements Initializable {

    @FXML private AnchorPane rootPane;

    @FXML private TableView<LegalPerson> tableView;
    @FXML private TableColumn<LegalPerson, String> collumnIco;
    @FXML private TableColumn<LegalPerson, String> collumnDic;
    @FXML private TableColumn<LegalPerson, String> collumnName;
    @FXML private TableColumn<LegalPerson, String> collumnAdress;
    @FXML private TableColumn<LegalPerson, String> collumnBankAccount;
    @FXML private TableColumn<LegalPerson, String> collumnPhoneNumber;

    private LegalPerson legalPerson;
    private Boolean dataChanged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnIco.setCellValueFactory(new PropertyValueFactory<>("ID"));
        collumnDic.setCellValueFactory(new PropertyValueFactory<>("dic"));
        collumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        collumnAdress.setCellValueFactory(new PropertyValueFactory<>("adress"));
        collumnBankAccount.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));
        collumnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        tableView.setEditable(true);
        collumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnAdress.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnBankAccount.setCellFactory(TextFieldTableCell.forTableColumn());
        collumnPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public Boolean getDataChanged() {
        return dataChanged;
    }

    public void setLegalPerson(LegalPerson legalPerson) {
        this.legalPerson = legalPerson;
        tableView.setItems(FXCollections.observableArrayList(legalPerson));
    }

    public void changeNameCell(TableColumn.CellEditEvent<LegalPerson, String> legalPersonStringCellEditEvent) {
        dataChanged = true;
        legalPerson.setName(legalPersonStringCellEditEvent.getNewValue());
    }

    public void changeAdressCell(TableColumn.CellEditEvent<LegalPerson, String> legalPersonStringCellEditEvent) {
        dataChanged = true;
        legalPerson.setAdress(legalPersonStringCellEditEvent.getNewValue());
    }

    public void changeBankAccountCell(TableColumn.CellEditEvent<LegalPerson, String> legalPersonStringCellEditEvent) {
        dataChanged = true;
        legalPerson.setBankAccount(legalPersonStringCellEditEvent.getNewValue());
    }

    public void changePhoneNumberCell(TableColumn.CellEditEvent<LegalPerson, String> legalPersonStringCellEditEvent) {
        dataChanged = true;
        legalPerson.setPhoneNumber(legalPersonStringCellEditEvent.getNewValue());
    }
}
