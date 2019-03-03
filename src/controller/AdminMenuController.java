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

public class AdminMenuController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelSecondName;
    @FXML private Label labelDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // labelFirstName.setText();
        //labelSecondName.setText();
        labelDate.setText(LocalDate.now().toString());
    }

    public void btnAddEmployeePushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_add_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnSearchEmployeePushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_search_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnLogOutPushed(ActionEvent actionEvent) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
