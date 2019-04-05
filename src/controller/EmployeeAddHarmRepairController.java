package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeAddHarmRepairController implements Initializable {
    @FXML private AnchorPane rootPane;

    @FXML private JFXTextField textFieldService;
    @FXML private JFXTextField textFieldPrice;
    @FXML private JFXDatePicker datePickerDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnAddRepairPushed(ActionEvent actionEvent) {
    }
}
