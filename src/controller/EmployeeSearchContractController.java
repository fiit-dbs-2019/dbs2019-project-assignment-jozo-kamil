package controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import model.*;
import org.controlsfx.control.Notifications;
import persistancemanagers.CarManager;
import persistancemanagers.CreateContractManager;
import persistancemanagers.EmployeeManager;
import persistancemanagers.PersonManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeSearchContractController implements Initializable {
    @FXML private TableColumn<Contract, Date> collumnDateOfCreating;
    @FXML private AnchorPane rootPane;
    @FXML private Label labelFirstName;
    @FXML private Label labelDate;
    @FXML private Label labelLastName;
    @FXML private TableView<Contract> tableView;
    @FXML private TableColumn<Contract, Integer> collumnNumberOfContract;
    @FXML private TableColumn<Contract, String> collumnID;
    @FXML private TableColumn<Contract, String> collumnVIN;
    @FXML private TextField textFieldSearchInTables;
    @FXML private Button buttonPreviousData;
    @FXML private Button buttonNextData;
    @FXML private Label labelOffset;
    @FXML private TextField textFieldSearchInDatabase;
    @FXML private JFXProgressBar progressBar;

    private ObservableList<Contract> observableList;

    private FilteredList filter;

    private Employee employee;

    private Integer offSet = 0;

    private Boolean isButtonSearchInDatabasePushed = false;

    private Boolean isButtonSearchEmployeeContractsPushed = false;

    public void setHeader () {
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        labelDate.setText(time);
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        setHeader();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collumnNumberOfContract.setCellValueFactory(new PropertyValueFactory<>("contract_id"));
        collumnID.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        collumnDateOfCreating.setCellValueFactory(new PropertyValueFactory<>("date_of_creating"));
        collumnVIN.setCellValueFactory(new PropertyValueFactory<>("car_vin"));

        // enable funcionality of ordering the collumns by click on it
        collumnVIN.setSortType(TableColumn.SortType.ASCENDING);
        collumnNumberOfContract.setSortType(TableColumn.SortType.ASCENDING);
        collumnDateOfCreating.setSortType(TableColumn.SortType.ASCENDING);
        collumnID.setSortType(TableColumn.SortType.ASCENDING);

        tableView.getSortOrder().add(collumnVIN);
        tableView.getSortOrder().add(collumnDateOfCreating);
        tableView.getSortOrder().add(collumnID);
        tableView.getSortOrder().add(collumnNumberOfContract);

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

    public void setNewRangeOfDisplayedData() {
        int range = offSet + observableList.size();
        setLabelOffset(((offSet + 1) + " - " + range));
    }

    public void addItemsToList() {
        CreateContractManager contractManager = new CreateContractManager();
        observableList = contractManager.getContract(offSet);
    }

    public void addItemsToTable() {
        tableView.setItems(observableList);

        filter = new FilteredList(observableList,e->true);

        if(observableList.size() == 500) {
            buttonNextData.setDisable(false);
        }
    }

    public void btnBackPushed(ActionEvent actionEvent) {
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

    public void detailMenuSelected(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/employee_contract_detail.fxml"));

        EmployeeContractDetailController employeeContractDetailController;

        try {
            Parent detailWindow = (Parent) loader.load();

            employeeContractDetailController = loader.getController();

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Detail");
            stage.setScene(new Scene(detailWindow));

            Task getFromDatabase = new Task() {
                @Override
                protected Object call() throws Exception {
                    progressBar.setVisible(true);

                    Contract contractDetail = tableView.getSelectionModel().getSelectedItem();
                    employeeContractDetailController.setContract(contractDetail);

                    PersonManager personManager = new PersonManager();
                    Person personFromContract = personManager.getPersonFromDatabase(contractDetail.getCustomer_id());
                    employeeContractDetailController.setPerson(personFromContract);

                    CarManager carManager = new CarManager();
                    Car carFromContract = carManager.getCarFromDatabase(contractDetail.getCar_vin());
                    //carManager.getCarInfo(carFromContract);
                    employeeContractDetailController.setCar(carFromContract);

                    EmployeeManager employeeManager = new EmployeeManager();
                    Employee employeeFromContract = employeeManager.getEmployeeFromDatabase(contractDetail.getEmployee_id());
                    employeeContractDetailController.setEmployee(employeeFromContract);

                    return null;
                }
            };

            getFromDatabase.setOnSucceeded(event -> {
                progressBar.setVisible(false);

                employeeContractDetailController.setInfo();
                stage.show();
            });

            Thread thread = new Thread(getFromDatabase);
            thread.setDaemon(true);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchInTable(KeyEvent keyEvent) {
        if(observableList == null) {
            return;
        }

        textFieldSearchInTables.textProperty().addListener((observable, oldvalue, newValue) -> {
            filter.setPredicate((Predicate<? super Contract>) contract ->{
                if(newValue == null ||  newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if(contract.getCar_vin().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(contract.getCustomer_id().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Contract> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        setNewRangeOfDisplayedData();
    }

    public void buttonDisplayDataPushed(ActionEvent actionEvent) {
        isButtonSearchInDatabasePushed = false;
        isButtonSearchEmployeeContractsPushed = false;

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

    public void loadNext(ActionEvent actionEvent) {
        offSet += 500;

        if(offSet > 0) {
            buttonPreviousData.setDisable(false);
        }

        Task loadNext = new Task() {
            @Override
            protected Object call() throws Exception {
                progressBar.setVisible(true);

                CreateContractManager contractManager = new CreateContractManager();

                if(isButtonSearchInDatabasePushed) {
                    observableList = contractManager.getContractByAtributes(getTextFieldSearchInDatabase(),offSet);
                } else if(isButtonSearchEmployeeContractsPushed) {
                    observableList = contractManager.getContractForSpecificEmployee(employee,offSet);
                } else {
                    observableList = contractManager.getContract(offSet);
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

                CreateContractManager contractManager = new CreateContractManager();

                if(isButtonSearchInDatabasePushed) {
                    observableList = contractManager.getContractByAtributes(getTextFieldSearchInDatabase(),offSet);
                } else if(isButtonSearchEmployeeContractsPushed) {
                    observableList = contractManager.getContractForSpecificEmployee(employee,offSet);
                }else {
                    observableList = contractManager.getContract(offSet);
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

    public void buttonSearchInDatabasePushed(ActionEvent actionEvent) {
        buttonPreviousData.setDisable(true);
        isButtonSearchInDatabasePushed = true;
        offSet = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected Object call() throws Exception {

                progressBar.setVisible(true);

                CreateContractManager contractManager = new CreateContractManager();
                observableList = contractManager.getContractByAtributes(getTextFieldSearchInDatabase(),offSet);

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

    public void buttonDisplayMyContractsPushed(ActionEvent actionEvent) {
        buttonPreviousData.setDisable(true);
        isButtonSearchEmployeeContractsPushed = true;
        offSet = 0;

        Task addDataFromDatabase = new Task() {
            @Override
            protected Object call() throws Exception {

                progressBar.setVisible(true);

                CreateContractManager contractManager = new CreateContractManager();
                observableList = contractManager.getContractForSpecificEmployee(employee,offSet);

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

    public void deleteMenuSelected(ActionEvent actionEvent) {
        Contract selectedContract = tableView.getSelectionModel().getSelectedItem();

        CreateContractManager contractManager = new CreateContractManager();

        if(contractManager.deleteContract(selectedContract)) {

            Notifications notification = Notifications.create()
                    .title("Záznam bol úspešne odstránený!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showConfirm();

            tableView.getItems().remove(selectedContract);
        } else {
            Notifications notification = Notifications.create()
                    .title("Záznam sa nepodarilo odstrániť!")
                    .hideAfter(Duration.seconds(4))
                    .hideCloseButton();
            notification.showError();
        }
    }
}
