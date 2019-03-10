package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeAddLegalPersonController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;
    @FXML private TextField textFieldICO;
    @FXML private TextField textFieldDIC;
    @FXML private TextField testFieldName;
    @FXML private TextField textFieldAdress;
    @FXML private TextField textFieldBankAccount;
    @FXML private TextField textFieldPhone;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelDate.setText(LocalDate.now().toString());
    }

    public void btnAddPushed(ActionEvent actionEvent) {
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_customer.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
