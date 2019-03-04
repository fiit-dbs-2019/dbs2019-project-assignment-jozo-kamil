package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAgreementController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private Label labelDateFrom;
    @FXML private Label labelDateTo;
    @FXML private Label labelFirstName;
    @FXML private Label labelSecondName;
    @FXML private Label labelDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnLegalPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/legal_person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnNaturalPushed(ActionEvent actionEvent) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/natural_person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
