package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Employee;
import persistancemanagers.EmployeeManager;
import persistancemanagers.EnumManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AdminAddEmployeeController implements Initializable {


    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;
    @FXML private TextField textFieldFirstName;
    @FXML private TextField textFieldLastName;
    @FXML private TextField textFieldLogin;
    @FXML private TextField textFieldPassword;
    @FXML private TextField textPhoneNumber;
    @FXML private ComboBox comboBoxType;

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
        addItemsComboBox();
    }

    public void addItemsComboBox() {
        try {
            EnumManager em = new EnumManager();

            em.employeeTypeEnum(comboBoxType,"employee_type");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFirstName() {
        return textFieldFirstName.getText();
    }

    public String getLastName() {
        return textFieldLastName.getText();
    }

    public String getLogin() {
        return textFieldLogin.getText();
    }

    public String getPassword() {
        return textFieldPassword.getText();
    }

    public String getPhone() { return textPhoneNumber.getText(); }

    public String getType() {
        return comboBoxType.getSelectionModel().getSelectedItem().toString();
    }

    public void btnAddEmployeePushed(ActionEvent actionEvent) throws SQLException {
        EmployeeManager em = new EmployeeManager();
        if(em.AddNewEmployeeToDatabase(getFirstName(),getLastName(),getLogin(),getPassword(), getPhone(), getType())){
            System.out.println("OK");
        } else {
            System.out.println("BAD");
        }

    }

    public void btnBackPushed(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/admin_menu.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
