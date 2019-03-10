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
import java.util.ResourceBundle;

public class EmployeeLegalPersonController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelSecondName;
    @FXML private Label labelDate;
    @FXML private TextField textIco;
    @FXML private TextField textDic;
    @FXML private TextField textNameCompany;
    @FXML private TextField textLocation;
    @FXML private TextField textBankAccount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/create_agreement.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnCreateAgreementPushed(ActionEvent actionEvent) {
    }
}
