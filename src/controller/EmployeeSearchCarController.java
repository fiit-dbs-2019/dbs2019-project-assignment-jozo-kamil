package controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Car;
import model.Employee;
import model.Person;
import org.controlsfx.control.Notifications;
import persistancemanagers.CarManager;
import persistancemanagers.EmployeeManager;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeSearchCarController implements Initializable {

    @FXML private AnchorPane rootPane;

    // header labels
    @FXML private Label labelFirstName;
    @FXML private Label labelLastName;
    @FXML private Label labelDate;

    // table view and collumns
    @FXML private TableView<Car> tableView;
    @FXML private TableColumn<Car, String> collumnVIN;
    @FXML private TableColumn<Car, String> collumnSPZ;
    @FXML private TableColumn<Car, Date> collumnYearOfProduction;
    @FXML private TableColumn<Car, Integer> collumnMileage;

    // textfields for search
    @FXML private TextField textFieldSearchInTables;
    @FXML private TextField textFieldSearchInDatabase;

    private ObservableList<Car> observableList;

    private FilteredList filter;

    @FXML private Button buttonNextData;
    @FXML private Button buttonPreviousData;
    @FXML private Label labelOffset;

    @FXML private JFXProgressBar progressBar;

    private Employee employee;

    private Person customer = null;
    private String customerID = null;

    private Integer offSet = 0;

    private Boolean isButtonSearchInDatabasePushed = false;

    public void setHeader () {
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnVIN.setCellValueFactory(new PropertyValueFactory<>("car_vin"));
        collumnSPZ.setCellValueFactory(new PropertyValueFactory<>("spz"));
        collumnYearOfProduction.setCellValueFactory(new PropertyValueFactory<>("year_of_production"));
        collumnMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        // enable funcionality of ordering the collumns by click on it
        collumnVIN.setSortType(TableColumn.SortType.ASCENDING);
        collumnSPZ.setSortType(TableColumn.SortType.ASCENDING);
        collumnYearOfProduction.setSortType(TableColumn.SortType.ASCENDING);
        collumnMileage.setSortType(TableColumn.SortType.ASCENDING);

        tableView.getSortOrder().add(collumnVIN);
        tableView.getSortOrder().add(collumnSPZ);
        tableView.getSortOrder().add(collumnYearOfProduction);
        tableView.getSortOrder().add(collumnMileage);

        buttonNextData.setDisable(true);
        buttonPreviousData.setDisable(true);

        progressBar.setVisible(false);
    }

    public String getTextFieldSearchInDatabase() {
        return textFieldSearchInDatabase.getText();
    }

    public void setLabelOffset(String labelOffset) {
        this.labelOffset.setText(labelOffset);
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setHeader();
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setNewRangeOfDisplayedData() {
        int range = offSet + observableList.size();
        setLabelOffset(((offSet + 1) + " - " + range));
    }

    // add items

    public void addItemsToList() {
        CarManager carManager = new CarManager();
        observableList = carManager.getCars(offSet);
    }

    public void addItemsToTable() {
        tableView.setItems(observableList);

        filter = new FilteredList(observableList,e->true);

        if(observableList.size() == 500) {
            buttonNextData.setDisable(false);
        }
    }

    @FXML
    public void btnBackPushed(ActionEvent event) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_search_menu.fxml"));
            parent = (Parent) loaader.load();

            EmployeeSearchMenuController employeeSearchMenuController = loaader.getController();
            employeeSearchMenuController.setEmployee(employee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }

    @FXML
    public void loadNext(ActionEvent event) {
        offSet += 500;

        if(offSet > 0) {
            buttonPreviousData.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected Object call() throws Exception {
                progressBar.setVisible(true);

                CarManager carManager = new CarManager();

                if(isButtonSearchInDatabasePushed) {
                    observableList = carManager.getCarsByVINorSPZ(getTextFieldSearchInDatabase(),offSet);
                } else {
                    observableList = carManager.getCars(offSet);
                }

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

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadNext);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void loadPrevious(ActionEvent event) {

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

                if(isButtonSearchInDatabasePushed) {
                    observableList = carManager.getCarsByVINorSPZ(getTextFieldSearchInDatabase(),offSet);
                } else {
                    observableList = carManager.getCars(offSet);
                }

                return observableList;
            }
        };

        loadPrevious.setOnSucceeded(event1 -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            filter = new FilteredList(observableList,e->true);

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(loadPrevious);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void searchInTable(KeyEvent event) {

        if(observableList == null) {
            return;
        }

        textFieldSearchInTables.textProperty().addListener((observable, oldvalue, newValue) -> {
            filter.setPredicate((Predicate<? super Car>) car ->{
                if(newValue == null ||  newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if(car.getCar_vin().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(car.getSpz().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Car> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        setNewRangeOfDisplayedData();
    }

    @FXML
    public void buttonSearchInDatabasePushed(ActionEvent event) {
        buttonPreviousData.setDisable(true);
        isButtonSearchInDatabasePushed = true;
        offSet = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected Object call() throws Exception {

                progressBar.setVisible(true);

                CarManager carManager = new CarManager();
                observableList = carManager.getCarsByVINorSPZ(getTextFieldSearchInDatabase(),offSet);

                return observableList;
            }
        };

        addDataFromDatabase.setOnSucceeded(event1 -> {
            tableView.setItems(observableList);

            progressBar.setVisible(false);

            if(observableList.size() == 500) {
                Notifications notification = Notifications.create()
                        .title("Počet nájdených záznamov je väčší ako " + observableList.size() + ".")
                        .hideAfter(Duration.seconds(4))
                        .hideCloseButton();
                notification.showInformation();

                buttonNextData.setDisable(false);
            } else {
                buttonNextData.setDisable(true);
            }

            filter = new FilteredList(observableList,e->true);

            textFieldSearchInTables.setText("");

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(addDataFromDatabase);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void detailMenuSelected(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_car_detail.fxml"));

        EmployeeCarDetailController employeeCarDetailController;

        try {
            Parent detailWindow = (Parent) loader.load();

            employeeCarDetailController = loader.getController();
            Car selectedCar = tableView.getSelectionModel().getSelectedItem();

            CarManager carManager = new CarManager();
            carManager.getCarInfo(selectedCar);

            employeeCarDetailController.setCar(selectedCar);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Detail");
            stage.setScene(new Scene(detailWindow));

            // on hiding, there is a funcionality to add changes into database, if they were set
            stage.setOnHiding(event1 -> {
                Task updateInfo = new Task() {
                    @Override
                    protected Object call() throws Exception {

                        if(employeeCarDetailController.getDataChanged()) {
                            carManager.updateCarInfo(selectedCar);
                        }

                        return null;
                    }
                };

                updateInfo.setOnSucceeded(event2 -> {
                    if(employeeCarDetailController.getDataChanged()) {
                        Notifications notification = Notifications.create()
                                .title("Informácie boli úspešne aktualizované!")
                                .hideAfter(Duration.seconds(4))
                                .hideCloseButton();
                        notification.showConfirm();
                    }
                });

                Thread thread = new Thread(updateInfo);
                thread.setDaemon(true);
                thread.start();
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void buttonDisplayDataPushed(ActionEvent event) {
        isButtonSearchInDatabasePushed = false;

        offSet = 0;

        buttonPreviousData.setDisable(true);

        Task displayData = new Task() {
            @Override
            protected Object call() throws Exception {
                progressBar.setVisible(true);

                addItemsToList();

                return null;
            }
        };

        displayData.setOnSucceeded(event1 -> {
            addItemsToTable();

            filter = new FilteredList(observableList,e->true);

            progressBar.setVisible(false);

            textFieldSearchInTables.setText("");

            if(observableList.size() == 500) {
                buttonNextData.setDisable(false);
            }

            setNewRangeOfDisplayedData();
        });

        Thread thread = new Thread(displayData);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void createContractMenuSelected(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            FXMLLoader loaader = new FXMLLoader(getClass().getResource("../view/employee_create_contract.fxml"));
            parent = (Parent) loaader.load();

            EmployeeCreateContractController employeeCreateContractController= loaader.getController();
            employeeCreateContractController.setEmployee(employee);

            Car selectedCar = tableView.getSelectionModel().getSelectedItem();

            // get car info, because there is not all attributes set
            CarManager carManager = new CarManager();
            carManager.getCarInfo(selectedCar);

            if(customer != null) {
                employeeCreateContractController.setCustomer(customer);
                employeeCreateContractController.setCustomerID();
            } else if (customerID != null) {
                employeeCreateContractController.setSelectedCustomerID(customerID);
            }

            employeeCreateContractController.setCar(selectedCar);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene = new Scene(parent);

        //This line gets the Stage information
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        currentStage.setScene(newScene);
        currentStage.show();
    }
}
