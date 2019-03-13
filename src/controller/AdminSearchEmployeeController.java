package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import model.Employee;
import persistancemanagers.EmployeeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AdminSearchEmployeeController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;
    @FXML private ListView listView;

    public void setHeader () {
        EmployeeManager em = new EmployeeManager();

        try {
            em.setHeader(labelFirstName,labelLastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setHeader();
    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void btnDeleteEmployeePushed(ActionEvent actionEvent) {
        // TODO
    }
}
