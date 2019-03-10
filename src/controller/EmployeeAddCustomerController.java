package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeAddCustomerController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelDate.setText(LocalDate.now().toString());
    }

    public void btnLegalPersonPushed(ActionEvent actionEvent) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_legal_person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnNaturalPersonPushed(ActionEvent actionEvent) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_add_natural_person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/employee_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
