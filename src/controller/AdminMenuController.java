package controller;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Employee;
import persistancemanagers.EmployeeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML private Button btnSearchEmployee;
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;

    private Employee admin;

    public void setHeader () {
//        EmployeeManager em = new EmployeeManager();
//        try {
//            em.setHeader(labelFirstName,labelLastName);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        labelFirstName.setText(admin.getFirstName());
        labelLastName.setText(admin.getLastName());

        //String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        // https://stackoverflow.com/questions/38566638/javafx-displaying-time-and-refresh-in-every-second
        final DateFormat format = DateFormat.getInstance();
        labelDate.setText(format.format(Calendar.getInstance().getTime()));
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> {
                    final Calendar cal = Calendar.getInstance();
                    labelDate.setText(format.format(cal.getTime()));
                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setHeader();
        btnSearchEmployee.setDisable(true);                             // will be able to click soon
    }

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
        setHeader();
    }

    public void btnAddEmployeePushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/admin_add_employee.fxml"));
            parent = (Parent) loaader.load();

            AdminAddEmployeeController adminAddEmployeeController = loaader.getController();
            adminAddEmployeeController.setAdmin(admin);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnSearchEmployeePushed(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/admin_search_employee.fxml"));
            parent = (Parent) loaader.load();

            AdminSearchEmployeeController adminSearchEmployeeController = loaader.getController();
            adminSearchEmployeeController.setAdmin(admin);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnLogOutPushed(ActionEvent actionEvent) {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rootPane.getChildren().setAll(pane);
    }
}
