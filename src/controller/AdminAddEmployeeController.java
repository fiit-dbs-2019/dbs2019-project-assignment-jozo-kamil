package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminAddEmployeeController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelSecondName;
    @FXML private Label labelDate;
    @FXML private TextField textFieldFirstName;
    @FXML private TextField textFieldSecondName;
    @FXML private TextField textFieldLogin;
    @FXML private TextField textFieldPassword;
    @FXML private ChoiceBox chBoxType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // labelFirstName.setText();
        //labelSecondName.setText();
        labelDate.setText(LocalDate.now().toString());
    }

    public void btnAddEmployeePushed(ActionEvent actionEvent) throws IOException {
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
