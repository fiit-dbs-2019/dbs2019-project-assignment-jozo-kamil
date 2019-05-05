package controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Car;
import model.Employee;
import persistancemanagers.CarManager;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class EmployeeCarFromContractStatisticController implements Initializable {

    @FXML private Label labelTitle;

    @FXML private TableView<Car> tableView;

    @FXML private TableColumn<Car, String> collumnVin;
    @FXML private TableColumn<Car, Integer> collumnTotalPrice;

    @FXML private Label labelOffset;
    @FXML private Button buttonPreviousData;
    @FXML private Button buttonNextData;

    @FXML private AnchorPane rootPane;

    @FXML private Label labelFirstName;
    @FXML private Label labelDate;
    @FXML private Label labelLastName;

    @FXML private Spinner spinnerTotalPrice;

    private Employee employee;

    private Integer offSet = 0;

    private FilteredList filter;

    private ObservableList<Car> observableList;

    private Integer totalRepairsPrice = 1000;

    @FXML private JFXProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerTotalPrice.setValueFactory(new
                SpinnerValueFactory.IntegerSpinnerValueFactory
                (500,50000,1000,500));

        collumnVin.setCellValueFactory(new PropertyValueFactory<>("car_vin"));
        collumnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("sum"));

        collumnVin.setSortType(TableColumn.SortType.ASCENDING);
        collumnTotalPrice.setSortType(TableColumn.SortType.ASCENDING);

        tableView.getSortOrder().add(collumnVin);
        tableView.getSortOrder().add(collumnTotalPrice);

        buttonNextData.setDisable(true);
        buttonPreviousData.setDisable(true);

        progressBar.setVisible(false);
    }

    public void setHeader () {
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setHeader();
    }

    public void backToMenu() {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_statistics_menu.fxml"));
            parent = (Parent) loaader.load();

            EmployeeStatisticsMenuController employeeStatisticsMenuController = loaader.getController();
            employeeStatisticsMenuController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void btnBackPushed(ActionEvent actionEvent) {
        backToMenu();
    }

    public void loadNext(ActionEvent actionEvent) {
        offSet += 500;

        if(offSet > 0) {
            buttonPreviousData.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected Object call() throws Exception {
                progressBar.setVisible(true);

                CarManager carManager = new CarManager();


                observableList = carManager.getCarForStatisticNumberTwo(offSet, totalRepairsPrice);


                return observableList;
            }
        };

        loadNext.setOnSucceeded(event1 -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            if(observableList.size() < 500) {
                buttonNextData.setDisable(true);
            }

            filter = new FilteredList(observableList,e->true);


            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadNext);
        thread.setDaemon(true);
        thread.start();
    }

    public void loadPrevious(ActionEvent actionEvent) {
        if(offSet > 0) {
            buttonNextData.setDisable(false);
        }

        offSet -= 500;

        if(offSet == 0) {
            buttonPreviousData.setDisable(true);
        }

        Task loadPrevious = new Task() {
            @Override
            protected Object call() {

                progressBar.setVisible(true);

                CarManager carManager = new CarManager();


                observableList = carManager.getCarForStatisticNumberTwo(offSet, totalRepairsPrice);


                return observableList;
            }
        };

        loadPrevious.setOnSucceeded(event1 -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            filter = new FilteredList(observableList,e->true);


            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadPrevious);
        thread.setDaemon(true);
        thread.start();
    }

    public void setNewRangeOfDisplayedData() {
        int range = offSet + observableList.size();
        setLabelOffset(((offSet + 1) + " - " + range));
    }

    public void addItemsToList() {
        CarManager carManager = new CarManager();
        observableList = carManager.getCarForStatisticNumberTwo(offSet, totalRepairsPrice);
    }

    public void addItemsToTable() {
        tableView.setItems(observableList);

        filter = new FilteredList(observableList, e->true);

        if(observableList.size() == 500) {
            buttonNextData.setDisable(false);
        }
    }

    public void setLabelOffset(String labelOffset) {
        this.labelOffset.setText(labelOffset);
    }

    public void setLabelTitle() {
        this.labelTitle.setText("Vozidlá s celkovou hodnotou opráv väčšou ako " + totalRepairsPrice);
    }

    public void refreshTable() {
        addItemsToList();
        addItemsToTable();
        setNewRangeOfDisplayedData();
        setLabelTitle();
    }

    public void buttonSearchPushed(ActionEvent actionEvent) {
        totalRepairsPrice = Integer.parseInt(spinnerTotalPrice.getValue().toString());

        Task setInfo = new Task() {
            @Override
            protected Object call() {
                progressBar.setVisible(true);

                addItemsToList();

                return null;
            }
        };

        setInfo.setOnSucceeded(event -> {
            addItemsToTable();
            setNewRangeOfDisplayedData();
            setLabelTitle();

            progressBar.setVisible(false);

        });

        Thread thread = new Thread(setInfo);
        thread.setDaemon(true);
        thread.start();
    }
}
